import "./MessageInput.css"

export default function MessageInput(props: {receiverId: number}) {
    return (
        <div>
            <iframe name="dummyFrame" id="dummyFrame"/>

            <form method="post" name="send-message" autoComplete="off" action="http://localhost:8080/sendMessage">
                <div>
                    <input className="messageInput" type="text" name="message"/>
                    <input className="sendMessage" type="submit" value="Send"/>
                </div>
                <input type="hidden" name="receiver" value={props.receiverId}/>
            </form>
        </div>
    );
}