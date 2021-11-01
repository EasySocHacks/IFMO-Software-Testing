import "./UserListElement.css"

export interface UserListElementInterface {
    id: number,
    name: string,
    age: number,
    isFriend: boolean
}

export default function UserListElement(props: UserListElementInterface) {
    if (props.isFriend) {
        return (
            <div className="userListElementDiv">
                <b>{props.name}#{props.id}</b>
                <iframe name="dummyFrame" id="dummyFrame"/>

                <form method="post" name="remove-friend" action={"http://localhost:8080/friends/remove/" + props.id} target="dummyFrame">
                    <input type="submit" value="Remove from friends"/>
                </form>
            </div>
        );
    } else {
        return (
            <div className="userListElementDiv">
                <b>{props.name}#{props.id}</b>
                <iframe name="dummyFrame" id="dummyFrame"/>

                <form method="post" name="add-friend" action={"http://localhost:8080/friends/add/" + props.id} target="dummyFrame">
                    <input type="submit" value="Add to friends"/>
                </form>
            </div>
        );
    }
}