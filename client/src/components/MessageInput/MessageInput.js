import "./MessageInput.css"

function refresh() {
    window.location.reload()
}

export default function MessageInput(props: { receiverId: number }) {
    return (
        <div>
            <iframe title="MessageInputIFrame" name="dummyFrame" id="dummyFrame"/>

            <form method="post" name="send-message" autoComplete="off" action="http://localhost:8080/sendMessage"
                  target="dummyFrame" onSubmit={refresh}>
                <div>
                    <input className="messageInput" type="text" name="message"/>
                    <input className="sendMessage" type="submit" value="Send"/>
                </div>
                <input type="hidden" name="receiver" value={props.receiverId}/>
            </form>
        </div>
    );
}