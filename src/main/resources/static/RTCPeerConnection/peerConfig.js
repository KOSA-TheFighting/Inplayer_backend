// let remoteStreamElement = document.querySelector('#remoteStream');
let localStreamElement = document.querySelector('#localStream');
const camKey = crypto.randomUUID();
let pcListMap = new Map();
let roomId;
let otherKeyList = [];
let localStream = undefined;
let stompClient;
const member_nickname = crypto.randomUUID(); //테스트용 임시 닉네임
let isBroadcaster = false; // 방송자/시청자 구분을 위한 플래그

const startCam = async () => {
    if(navigator.mediaDevices !== undefined){
        await navigator.mediaDevices.getUserMedia({ audio: true, video: true })
            .then(async (stream) => {
                console.log('Stream found');
                localStream = stream;
                stream.getAudioTracks()[0].enabled = true;
                localStreamElement.srcObject = localStream;
            }).catch(error => {
                console.error("Error accessing media devices:", error);
            });
    }
}

const connectSocket = async () => {
    const socket = new SockJS('/signaling');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({
		roomId: roomId,
		camKey: camKey
		}, async () => {
        console.log('Connected to WebRTC server');
        
        // 방송자 여부 확인을 위한 구독
		stompClient.subscribe('/topic/room/status/' + roomId, message => {
		    const roomStatus = JSON.parse(message.body);
		    console.log('[Room Status]', roomStatus);
		    
		    if (roomStatus.hasBroadcaster && !isBroadcaster) {
		        console.log('[Viewer] Found broadcaster:', roomStatus.broadcasterKey);
		        if (!otherKeyList.includes(roomStatus.broadcasterKey)) {
		            otherKeyList.push(roomStatus.broadcasterKey);
		        }
		        requestBroadcastStream();
		    }
		});

        // ICE candidate 구독
        stompClient.subscribe('/topic/peer/iceCandidate/' + camKey + '/' + roomId, candidate => {
            const key = JSON.parse(candidate.body).key;
            const message = JSON.parse(candidate.body).body;

            if (pcListMap.has(key)) {
                pcListMap.get(key).addIceCandidate(new RTCIceCandidate({
                    candidate: message.candidate,
                    sdpMLineIndex: message.sdpMLineIndex,
                    sdpMid: message.sdpMid
                }));
                console.log('Added ICE candidate for:', key);
            }
        });

        // Offer 구독
        stompClient.subscribe('/topic/peer/offer/' + camKey + '/' + roomId, async offer => {
            const key = JSON.parse(offer.body).key;
            const message = JSON.parse(offer.body).body;

            if (!pcListMap.has(key)) {
                pcListMap.set(key, createPeerConnection(key));
            }

            await pcListMap.get(key).setRemoteDescription(new RTCSessionDescription({
                type: message.type,
                sdp: message.sdp
            }));
            sendAnswer(pcListMap.get(key), key);
            console.log('Processed offer from:', key);
        });

        // Answer 구독
        stompClient.subscribe('/topic/peer/answer/' + camKey + '/' + roomId, answer => {
            const key = JSON.parse(answer.body).key;
            const message = JSON.parse(answer.body).body;

            pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(message));
            console.log('Processed answer from:', key);
        });

        // 채팅 구독
        stompClient.subscribe('/topic/chat/' + roomId, message => {
            const chatMessage = JSON.parse(message.body);
            const chatBox = document.querySelector('#chatBox');
            const newMessage = document.createElement('div');
            newMessage.textContent = '[' + chatMessage.sender + ']: ' + chatMessage.message;
            chatBox.appendChild(newMessage);
            chatBox.scrollTop = chatBox.scrollHeight;
        });

        // Key 관련 구독
        stompClient.subscribe('/topic/call/key', () => {
            stompClient.send('/app/send/key', {}, JSON.stringify(camKey));
            console.log('Sent camKey in response to call');
        });

        stompClient.subscribe('/topic/send/key', message => {
            const key = JSON.parse(message.body);
            console.log('Received key:', key, 'Current key:', camKey);
            
            if (key && camKey !== key) {
                if (!otherKeyList.includes(key)) {
                    console.log('Adding new key to list:', key);
                    otherKeyList.push(key);
                    
                    // 시청자이고 새로운 방송자가 들어왔을 때 자동으로 연결 시도
                    if (!isBroadcaster) {
                        const pc = createPeerConnection(key);
                        pcListMap.set(key, pc);
                        sendOffer(pc, key);
                        console.log('Created new peer connection for:', key);
                    }
                }
            }
        });

        // 방 상태 확인 요청
        await stompClient.send('/app/room/status/' + roomId, {}, {});
        
        // 방송자일 경우에만 방송 설정 버튼 표시
        if (isBroadcaster) {
            document.querySelector('#setupStreamBtn').style.display = 'block';
        }
    });
};

// 시청자의 스트림 요청 함수 수정
const requestBroadcastStream = async () => {
    console.log('[Viewer] Requesting broadcast stream...');
    
    // 방송자 키 요청
    await stompClient.send('/app/call/key', {}, {});
    console.log('[Viewer] Sent key request');
    
    // 방송자 연결 시도
    setTimeout(() => {
        console.log('[Viewer] Available broadcaster keys:', otherKeyList);
        if (otherKeyList.length === 0) {
            console.warn('[Viewer] No broadcaster keys available');
            // 방송자 키가 없으면 3초 후 재시도
            setTimeout(requestBroadcastStream, 3000);
            return;
        }

        otherKeyList.forEach((key) => {
            if(!pcListMap.has(key)){
                try {
                    console.log('[Viewer] Creating peer connection for broadcaster:', key);
                    const pc = createPeerConnection(key);
                    pcListMap.set(key, pc);
                    sendOffer(pc, key);
                } catch (error) {
                    console.error('[Viewer] Failed to create peer connection:', error);
                }
            }
        });
    }, 2000);
};


// 방송 시작 함수
const startBroadcasting = async () => {
    console.log('Starting broadcast...');
    
    // 먼저 방송 시작 상태를 서버에 알림
    stompClient.send('/app/room/broadcast/start/' + roomId, {}, JSON.stringify({
        broadcasterKey: camKey
    }));
    
    // 키 요청 전에 잠시 대기
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // 다른 참가자들에게 키 전송
    await stompClient.send('/app/call/key', {}, {});
    
    console.log('Waiting for other participants...');
    // 다른 참가자들과 연결 설정
    setTimeout(() => {
        console.log('Creating peer connections for:', otherKeyList);
        otherKeyList.forEach((key) => {
            if(!pcListMap.has(key)){
                const pc = createPeerConnection(key);
                pcListMap.set(key, pc);
                sendOffer(pc, key);
                console.log('Created peer connection for:', key);
            }
        });
    }, 2000);
};

const createPeerConnection = (otherKey) => {
    console.log('[PeerConnection] Creating for:', otherKey);
    const pc = new RTCPeerConnection({
        iceServers: [
            { urls: 'stun:stun1.l.google.com:19302' },
            { urls: 'stun:stun2.l.google.com:19302' },
            { urls: 'stun:stun3.l.google.com:19302' },
            { urls: 'stun:stun4.l.google.com:19302' },
            { urls: 'stun:stun5.l.google.com:19302' }
        ],
        iceCandidatePoolSize: 10,
        bundlePolicy: 'max-bundle',
        rtcpMuxPolicy: 'require',
        iceTransportPolicy: 'all' // 'all' 또는 'relay' 설정 가능
    });
    
    try {
        pc.addEventListener('icecandidate', (event) => {
            onIceCandidate(event, otherKey);
        });
        
        pc.addEventListener('track', (event) => {
            console.log('[PeerConnection] Received track:', event);
            onTrack(event, otherKey);
        });
        
        pc.addEventListener('connectionstatechange', () => {
            console.log('[PeerConnection] Connection state changed:', pc.connectionState);
            switch(pc.connectionState) {
                case 'connected':
                    console.log('[PeerConnection] Connection established successfully');
                    break;
                case 'disconnected':
                case 'failed':
                    console.log('[PeerConnection] Connection failed or disconnected, attempting reconnect...');
                    handleConnectionFailure(otherKey);
                    break;
                case 'closed':
                    console.log('[PeerConnection] Connection closed');
                    break;
            }
        });
        
        pc.addEventListener('icecandidateerror', (event) => {
            console.error('[ICE] Candidate error:', event);
        });

        pc.addEventListener('iceconnectionstatechange', () => {
            console.log('[ICE] Connection state:', pc.iceConnectionState);
            if (pc.iceConnectionState === 'failed') {
                console.log('[ICE] Connection failed, restarting ICE...');
                pc.restartIce();
            }
        });
        
        if(localStream !== undefined){
            localStream.getTracks().forEach(track => {
                pc.addTrack(track, localStream);
                console.log('[PeerConnection] Added local track:', track.kind);
            });
        }
        
        console.log('[PeerConnection] Created successfully');
    } catch (error) {
        console.error('[PeerConnection] Failed:', error);
    }
    return pc;
};

const handleConnectionFailure = (otherKey) => {
    const pc = pcListMap.get(otherKey);
    if (pc) {
        console.log('[Connection] Attempting to reconnect with:', otherKey);
        pc.close();
        pcListMap.delete(otherKey);
        removeVideoElement(otherKey);  // 비디오 요소 제거
        
        // 재연결 시도
        setTimeout(() => {
            const newPc = createPeerConnection(otherKey);
            pcListMap.set(otherKey, newPc);
            sendOffer(newPc, otherKey);
        }, 1000);
    }
};

const onTrack = (event, otherKey) => {
    console.log('[Stream] Received track from:', otherKey, event.streams);
    
    // CSS 선택자에서 사용할 수 있도록 ID 수정
    const safeId = `video-${otherKey.replace(/[^a-zA-Z0-9]/g, '-')}`;
    const existingVideo = document.querySelector(`#${safeId}`);
    
    if(!existingVideo){
        try {
            const video = document.createElement('video');
            video.autoplay = true;
            video.playsInline = true;
            video.controls = true;
            video.id = safeId;  // 안전한 ID 사용
            video.setAttribute('data-peer-id', otherKey);  // 원래 ID는 data 속성으로 저장
            video.srcObject = event.streams[0];
            
            // 비디오 요소 디버깅
            video.addEventListener('loadedmetadata', () => {
                console.log('[Video] Metadata loaded for:', otherKey);
                video.play()
                    .then(() => console.log('[Video] Playing video from:', otherKey))
                    .catch(e => console.error('[Video] Play failed:', e));
            });
            
            video.addEventListener('error', (e) => {
                console.error('[Video] Error:', e);
            });
            
            document.querySelector('#remoteStreamDiv').appendChild(video);
            console.log('[Stream] Created new video element for:', otherKey);
        } catch (error) {
            console.error('[Stream] Failed to set up video element:', error);
        }
    } else {
        try {
            existingVideo.srcObject = event.streams[0];
            console.log('[Stream] Updated existing video for:', otherKey);
        } catch (error) {
            console.error('[Stream] Failed to update video:', error);
        }
    }
};

// 비디오 요소 제거를 위한 헬퍼 함수 추가
const removeVideoElement = (otherKey) => {
    const safeId = `video-${otherKey.replace(/[^a-zA-Z0-9]/g, '-')}`;
    const video = document.querySelector(`#${safeId}`);
    if (video) {
        video.srcObject = null;
        video.remove();
        console.log('[Stream] Removed video element for:', otherKey);
    }
};

const onIceCandidate = (event, otherKey) => {
    if (event.candidate) {
        console.log('[ICE] New candidate:', event.candidate.type);
        stompClient.send('/app/peer/iceCandidate/' + otherKey + '/' + roomId, {}, JSON.stringify({
            key: camKey,
            body: event.candidate
        }));
    } else {
        console.log('[ICE] All candidates gathered');
    }
};

const sendOffer = (pc, otherKey) => {
    const offerOptions = {
        offerToReceiveAudio: true,
        offerToReceiveVideo: true,
        iceRestart: true // ICE 재시작 옵션 추가
    };

    pc.createOffer(offerOptions).then(offer => {
        console.log('[Offer] Created for:', otherKey);
        // SDP 수정하여 더 나은 호환성 제공
        offer.sdp = enhanceSdp(offer.sdp);
        setLocalAndSendMessage(pc, offer);
        stompClient.send('/app/peer/offer/' + otherKey + '/' + roomId, {}, JSON.stringify({
            key: camKey,
            body: offer
        }));
        console.log('[Offer] Sent to:', otherKey);
    }).catch(error => {
        console.error('[Offer] Failed to create:', error);
        handleConnectionFailure(otherKey);
    });
};

const sendAnswer = (pc, otherKey) => {
    pc.createAnswer().then(answer => {
        setLocalAndSendMessage(pc, answer);
        stompClient.send('/app/peer/answer/' + otherKey + '/' + roomId, {}, JSON.stringify({
            key: camKey,
            body: answer
        }));
        console.log('Send answer');
    });
};

const setLocalAndSendMessage = (pc, sessionDescription) => {
    pc.setLocalDescription(sessionDescription);
}
// SDP 향상을 위한 함수
const enhanceSdp = (sdp) => {
    let modifiedSdp = sdp;
    
    // UDP 우선순위 설정
    modifiedSdp = modifiedSdp.replace(
        /a=candidate.*udp.*typ host/ig,
        (match) => `${match} network-cost 50`
    );
    
    // 비디오 코덱 최적화
    if (modifiedSdp.includes('VP8')) {
        modifiedSdp = modifiedSdp.replace(
            /(m=video.*)\r\n/g,
            '$1\r\na=rtpmap:96 VP8/90000\r\na=rtcp-fb:96 nack\r\na=rtcp-fb:96 nack pli\r\na=rtcp-fb:96 ccm fir\r\n'
        );
    }
    
    return modifiedSdp;
};

// 이벤트 리스너 수정
document.querySelector('#enterRoomBtn').addEventListener('click', async () => {
    roomId = document.querySelector('#roomIdInput').value;
    if (!roomId) {
        alert('방 번호를 입력해주세요.');
        return;
    }

    // URL의 쿼리 파라미터에서 broadcaster 여부 확인
    const urlParams = new URLSearchParams(window.location.search);
    isBroadcaster = urlParams.get('type') === 'broadcaster';

    document.querySelector('#roomIdInput').disabled = true;
    document.querySelector('#enterRoomBtn').disabled = true;
    
    if (isBroadcaster) {
        document.querySelector('#setupStreamBtn').style.display = 'block';
    }
    
    await connectSocket();
});

document.querySelector('#setupStreamBtn').addEventListener('click', async () => {
    await startCam();
    if(localStream !== undefined) {
        document.querySelector('#localStream').style.display = 'block';
        document.querySelector('#broadcastBtn').style.display = 'block';
        document.querySelector('#setupStreamBtn').style.display = 'none';
    } else {
        alert("캠을 연결하지 못했습니다.");
    }
});

document.querySelector('#broadcastBtn').addEventListener('click', async () => {
    await startBroadcasting();
	alert("방송 시작");
    document.querySelector('#broadcastBtn').disabled = true;
});
// Chat functionality
document.querySelector('#sendChatBtn').addEventListener('click', sendMessage);
document.querySelector('#chatInput').addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        sendMessage();
    }
});

function sendMessage() {
    if (!member_nickname) {
        alert('로그인이 필요한 서비스입니다.');
        return;
    }
    
    const message = document.querySelector('#chatInput').value;
    if (message) {   
        const now = new Date();
        const date = now.toLocaleDateString('ko-KR', { 
            year: 'numeric', 
            month: '2-digit', 
            day: '2-digit' 
        });
        const time = now.toLocaleTimeString('ko-KR', { 
            hour: '2-digit', 
            minute: '2-digit',
            hour12: false 
        });

        const chatMessage = {
            type: 'CHAT',
            roomId: roomId,
            sender: member_nickname,
            message: message,
            time: date + " " + time
        };
        
        stompClient.send('/app/chat/' + roomId, {}, JSON.stringify(chatMessage));
        document.querySelector('#chatInput').value = '';
    }
}

// 방송 종료 시 호출되는 함수
window.addEventListener('beforeunload', () => {
    if (isBroadcaster) {
        stompClient.send('/app/room/broadcast/end/' + roomId, {}, {});
    }
    
    // 스트림 정리
    if (localStream) {
        localStream.getTracks().forEach(track => track.stop());
    }

    // 피어 연결 정리
    pcListMap.forEach((pc) => {
        pc.close();
    });
    pcListMap.clear();
});

// 에러 처리를 위한 함수
const handleError = (error) => {
    console.error('Error:', error);
    alert('오류가 발생했습니다: ' + error.message);
};

// 연결 끊김 처리
const handleDisconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            console.log('Disconnected from WebSocket');
        });
    }

    // 스트림 정리
    if (localStream) {
        localStream.getTracks().forEach(track => track.stop());
    }

    // 피어 연결 정리
    pcListMap.forEach((pc) => {
        pc.close();
    });
    pcListMap.clear();
};

// 재연결 시도
const reconnect = async () => {
    try {
        await connectSocket();
    } catch (error) {
        console.error('Reconnection failed:', error);
        setTimeout(reconnect, 5000); // 5초 후 재시도
    }
};