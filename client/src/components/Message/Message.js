import "./Message.css"

export interface MessageInterface {
    from: number,
    to: number,
    message: string
}

export default function Message(props: MessageInterface) {
    return (
        <div className="messageBox">
            {props.message}
        </div>
    );
}