import {render} from "@testing-library/react";
import Chat from "../../../components/Chat/Chat";

test("render chat contains previous messages", () => {
    render(<Chat messages={[{from: 1, to: 2, message: 'Hi'}]}/>);
    window.location.search = "?id=1";
    expect(document.getElementsByClassName("messageBoxLeft")[0]
        .getElementsByClassName("messageBox")[0].innerHTML).toEqual("Hi");
});