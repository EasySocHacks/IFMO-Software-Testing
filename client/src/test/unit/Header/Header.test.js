import {render} from "@testing-library/react";
import Header from "../../../components/Header/Header";

test("render header friends tab", () => {
    render(<Header/>);
    const friends = document.getElementsByClassName("friendsTabLink")[0];
    expect(friends.href).toEqual("http://localhost/friends");
    expect(friends.getElementsByClassName("friendsTab")[0].innerHTML).toEqual("Friends");
});

test("render header register tab", () => {
    render(<Header/>);
    const register = document.getElementsByClassName("registrationTabLink")[0];
    expect(register.href).toEqual("http://localhost/register");
    expect(register.getElementsByClassName("registrationTab")[0].innerHTML).toEqual("Register");
});

test("render header login tab", () => {
    render(<Header/>);
    const login = document.getElementsByClassName("loginTabLink")[0];
    expect(login.href).toEqual("http://localhost/login");
    expect(login.getElementsByClassName("loginTab")[0].innerHTML).toEqual("Login");
});