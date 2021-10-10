import {render, screen} from "@testing-library/react";
import FriendList from "../../../components/FriendList/FriendList";

test("expect friends title", () => {
    render(<FriendList friendList={[]}/>);
    const title = screen.getByText("Friends")
    expect(title).toBeInTheDocument();
})

test("expect correct friend id props in query url", () => {
    render(<FriendList friendList={[{id: 1, name: "A", age: 1}, {id: 3, name: "ZZ", age:4}]}/>);
    const links = document.getElementsByTagName("a");
    expect(links[0].href).toEqual("http://localhost/friend?id=1");
    expect(links[1].href).toEqual("http://localhost/friend?id=3");
})

test("expect correct friend name list", () => {
    render(<FriendList friendList={[{id: 1, name: "A", age: 1}, {id: 3, name: "ZZ", age:4}]}/>);
    const elements = document.getElementsByClassName("friendListElement");
    expect(elements[0].getElementsByTagName("a")[0].innerHTML).toEqual("A");
    expect(elements[1].getElementsByTagName("a")[0].innerHTML).toEqual("ZZ");
})