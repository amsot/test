package mvc.handler;

import java.util.ArrayList;
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
 
public class EcoHandler extends TextWebSocketHandler{
 
    private Logger logger = LoggerFactory.getLogger(EcoHandler.class);
     
    /**
     * 서버에 연결한 사용자들을 저장하는 리스트
     */
    private List<WebSocketSession> connectedUsers;
     
    public EcoHandler() {
        connectedUsers = new ArrayList<WebSocketSession>();
    }
     
    /**
     * 접속과 관련된 Event Method
     *
     * @param WebSocketSession 접속한 사용자
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//       super.afterConnectionEstablished(session);
       logger.info(session.getId()+"님 접속");
       logger.info("연결 IP: " + session.getRemoteAddress().getHostName());
       connectedUsers.add(session);
    }
 
    /**
     * 두 가지 이벤트를 처리
     *
     * 1. Send : 클라이언트가 서버에게 메시지를 보냄
     * 2. Emit : 서버에 연결되어 있는 클라이언트에게 메시지를 보냄
     *
     * @param WebSocketSession 메시지를 보낸 클라이언트
     * @param TextMessage 메시지의 내용
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
         
//    	super.handleTextMessage(session, message);
        logger.info(session.getId()+"님 메시지 전송" + message.getPayload());
        for(WebSocketSession webSocketSession : connectedUsers) {
        	if(!session.getId().equals(webSocketSession)) {
        		webSocketSession.sendMessage( new TextMessage(session.getRemoteAddress().getHostName()+ " -> " + message.getPayload()));
        	}
        }
    }
 
    /**
     * 클라이언트가 서버와 연결을 끊었을때 실행되는 메소드
     *
     * @param WebSocketSession 연결을 끊은 클라이언트
     * @param CloseStatus 연결 상태(확인 필요함)
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//    	super.afterConnectionClosed(session, status);
        logger.info(session.getId()+"님 종료");
        connectedUsers.remove(session);
    }
}