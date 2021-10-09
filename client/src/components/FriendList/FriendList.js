import "./FriendList.css"

export interface FriendListElementInterface {
    id: number,
    name: string,
    age: number
}

export interface FriendListInterface {
    friendList: FriendListElementInterface[]
}

export default function FriendList(props: FriendListInterface) {
    return (
        <div>
            <h3>Friends</h3>
            <ul className="friendList">
                {props.friendList.map(friendListElement => {
                    return (
                        <li className="friendListElement">
                            <a href={"/friend?id=" + friendListElement.id}>{friendListElement.name}</a>
                        </li>
                    );
                })}
            </ul>
        </div>
    );
}