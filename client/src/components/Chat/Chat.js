import type {MessageInterface} from "../Message/Message";
import Message from "../Message/Message";
import "./Chat.css"
import MessageInput from "../MessageInput/MessageInput";

export interface CharInterface {
    messages: MessageInterface[]
}

export default function Chat(props: CharInterface) {
    const queryParams = new URLSearchParams(window.location.search);
console.log(props.messages);
    return (
        <div>
            {props.messages.map((message) => {
                return (
                    (parseInt(queryParams.get("id")) === message.from) ?
                        <div className="shiftRight message">
                            <div className="messageBoxRight">
                                <Message from={message.from} to={message.to} message={message.message}/>
                            </div>
                        </div>
                    :
                        <div className="shiftLeft message">
                            <div className="messageBoxLeft">
                                <Message from={message.from} to={message.to} message={message.message}/>
                            </div>
                        </div>
                );
            })}

            <MessageInput receiverId={queryParams.get("id")}/>
        </div>
    );
}