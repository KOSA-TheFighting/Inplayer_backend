// let remoteStreamElement = document.querySelector('#remoteStream');
let localStreamElement = document.querySelector('#localStream');
const camKey = crypto.randomUUID();
let pcListMap = new Map();
let roomId;
let otherKeyList = [];
let localStream = undefined;
let stompClient;
const member_nickname = crypto.randomUUID(); //테스트용 임시 닉네임

const startCam = async () =>{
    if(navigator.mediaDevices !== undefined){
        await navigator.mediaDevices.getUserMedia({ audio: true, video : true })
            .then(async (stream) => {
                console.log('Stream found');
                localStream = stream;
                // Disable the microphone by default
                stream.getAudioTracks()[0].enabled = true;
                localStreamElement.srcObject = localStream;

            }).catch(error => {
                console.error("Error accessing media devices:", error);
            });
    }
}

const connectSocket = async () =>{
    const socket = new SockJS('/signaling');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({
		//'Authorization': 'Bearer ' + localStorage.getItem('jwtToken'), //WebSocket 연결 시 인증 헤더 추가
		roomId: roomId, 
		camKey: camKey
	}, 
	() => {
        console.log('Connected to WebRTC server');
        
        stompClient.subscribe('/topic/peer/iceCandidate/' + camKey + '/' + roomId, candidate => {
            const key = JSON.parse(candidate.body).key
            const message = JSON.parse(candidate.body).body;

            pcListMap.get(key).addIceCandidate(new RTCIceCandidate({
				candidate:message.candidate,
				sdpMLineIndex:message.sdpMLineIndex, 
				sdpMid:message.sdpMid
			}));
        });

        stompClient.subscribe('/topic/peer/offer/' + camKey + '/' + roomId, offer => {
            const key = JSON.parse(offer.body).key;
            const message = JSON.parse(offer.body).body;

            pcListMap.set(key,createPeerConnection(key));
            pcListMap.get(key).setRemoteDescription(new RTCSessionDescription({
				type:message.type, 
				sdp:message.sdp
			}));
            sendAnswer(pcListMap.get(key), key);
        });

        stompClient.subscribe('/topic/peer/answer/' + camKey + '/' + roomId, answer =>{
            const key = JSON.parse(answer.body).key;
            const message = JSON.parse(answer.body).body;

            pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(message));
        });
		
		stompClient.subscribe('/topic/chat/' + roomId, message => {
		    const chatMessage = JSON.parse(message.body);
		    const chatBox = document.querySelector('#chatBox');
		    const newMessage = document.createElement('div');
			newMessage.textContent = '[' + chatMessage.sender + ']: ' + chatMessage.message;
		    chatBox.appendChild(newMessage);
		    chatBox.scrollTop = chatBox.scrollHeight; // Auto-scroll to the latest message
		});

        stompClient.subscribe('/topic/call/key', () =>{
            stompClient.send('/app/send/key', {}, JSON.stringify(camKey));

        });

        stompClient.subscribe('/topic/send/key', message => {
            const key = JSON.parse(message.body);

			if (key && camKey !== key) {
			    if (!otherKeyList.includes(key)) {
			        otherKeyList.push(key);
			    }
			}
        });
    });
}

let onTrack = (event, otherKey) => {
	const existingVideo = document.querySelector('#otherKey');
	
    if(!existingVideo){
        const video =  document.createElement('video');

        video.autoplay = true;
        video.controls = true;
        video.id = otherKey;
        video.srcObject = event.streams[0];

        document.querySelector('#remoteStreamDiv').appendChild(video);
    } else {
		existingVideo.srcObject = event.streams[0];
	}
	// remoteStreamElement.srcObject = event.streams[0];
    // remoteStreamElement.play();
};

const createPeerConnection = (otherKey) =>{
    const pc = new RTCPeerConnection();
    try {
        pc.addEventListener('icecandidate', (event) =>{
            onIceCandidate(event, otherKey);
         });
        pc.addEventListener('track', (event) =>{
            onTrack(event, otherKey);
        }, { once: true });
        if(localStream !== undefined){
            localStream.getTracks().forEach(track => {
                pc.addTrack(track, localStream);
            });
        }
		
        console.log('PeerConnection created');
    } catch (error) {
        console.error('PeerConnection failed: ', error);
    }
    return pc;
}

let onIceCandidate = (event, otherKey) => {
    if (event.candidate) {
        console.log('ICE candidate');
        stompClient.send('/app/peer/iceCandidate/' + otherKey + '/' + roomId,{}, JSON.stringify({
            key : camKey,
            body : event.candidate
        }));
    }
};

let sendOffer = (pc ,otherKey) => {
    pc.createOffer().then(offer =>{
        setLocalAndSendMessage(pc, offer);
        stompClient.send('/app/peer/offer/' + otherKey + '/' + roomId, {}, JSON.stringify({
            key : camKey,
            body : offer
        }));
        console.log('Send offer');
    });
};

let sendAnswer = (pc,otherKey) => {
    pc.createAnswer().then( answer => {
        setLocalAndSendMessage(pc ,answer);
        stompClient.send('/app/peer/answer/' + otherKey + '/' + roomId, {}, JSON.stringify({
            key : camKey,
            body : answer
        }));
        console.log('Send answer');
    });
};

const setLocalAndSendMessage = (pc ,sessionDescription) =>{
    pc.setLocalDescription(sessionDescription);
}

//룸 번호 입력 후 웹소켓 실행
document.querySelector('#enterRoomBtn').addEventListener('click', async () =>{
/*  await startCam();

    if(localStream !== undefined){
        document.querySelector('#localStream').style.display = 'block';
        document.querySelector('#startSteamBtn').style.display = '';
    } else {
		alert("캠을 연결하지 못했습니다.");
	}*/
    roomId = document.querySelector('#roomIdInput').value;
    document.querySelector('#roomIdInput').disabled = true;
    document.querySelector('#enterRoomBtn').disabled = true;
	document.querySelector('#readySteamBtn').style.display = '';

    await connectSocket();
});

//캠 연결
document.querySelector('#readySteamBtn').addEventListener('click', async () =>{
	await startCam();

	if(localStream !== undefined){
        document.querySelector('#localStream').style.display = 'block';
        document.querySelector('#startSteamBtn').style.display = '';
    } else {
		alert("캠을 연결하지 못했습니다.");
	}
});

// 스트림 버튼 클릭시 , 다른 웹 key들 웹소켓을 가져 온뒤에 offer -> answer -> iceCandidate 통신
// peer 커넥션은 pcListMap 으로 저장

document.querySelector('#startSteamBtn').addEventListener('click', async () =>{
	await stompClient.send('/app/call/key', {}, {});
	
	setTimeout(() =>{
		otherKeyList.map((key) =>{
			if(!pcListMap.has(key)){
				pcListMap.set(key, createPeerConnection(key));
				sendOffer(pcListMap.get(key),key);
			}
		});
	},2000);
});

document.querySelector('#sendChatBtn').addEventListener('click', sendMessage);
document.querySelector('#chatInput').addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        sendMessage();
    }
});
/*      jwt토큰에서 닉네임 추출
function getMemberFromToken() {
    const token = localStorage.getItem('jwtToken');
    if (!token) return null;
    
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.member_nickname;
}
*/
function sendMessage() {
	//const member_nickname = getMemberFromToken();
    if (!member_nickname) {
        alert('로그인이 필요한 서비스입니다.');
		//로그인 페이지로 리다이렉트
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
