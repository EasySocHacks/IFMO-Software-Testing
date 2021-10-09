import "./MessageInput.css"

export default function MessageInput(props: {receiverId: number}) {
    return (
        <div>
            <form method="post" name="send-message" autoComplete="off" action={process.env.REACT_APP_SERVER_URL + "/sendMessage"}>
                <div>
                    <input className="messageInput" type="text" name="message"/>
                    <input className="sendMessage" type="submit" value="Send"/>
                </div>
                <input type="hidden" name="receiver" value={props.receiverId}/>
            </form>
        </div>
    );
}