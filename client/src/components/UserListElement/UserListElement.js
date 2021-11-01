import "./UserListElement.css"

function redirect() {
    window.location = "/"
}

export interface UserListElementInterface {
    id: number,
    name: string,
    age: number
}

export default function UserListElement(props: UserListElementInterface) {
    return (
        <div className="userListElementDiv">
            <b>{props.name}#{props.id}</b>
            <iframe title="AddFriendsIFrame" name="dummyFrame" id="dummyFrame"/>

            <form method="post" name="add-friend" action={process.env.REACT_APP_SERVER_URL + "/friends/add/" + props.id}
                  target="dummyFrame" onSubmit={redirect}>
                <input type="submit" value="Add to friends"/>
            </form>
        </div>
    );
}