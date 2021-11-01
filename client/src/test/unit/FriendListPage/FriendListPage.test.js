import {render, screen} from "@testing-library/react";
import FriendListPage from "../../../pages/FriendListPage/FriendListPage";

describe("<FriendListPage/>", () => {
    it('render friends title',  () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve([]),
            })
        );

        render(<FriendListPage/>)
        expect(screen.getByText("Friends")).toBeInTheDocument()
    });

    it('render empty friend list on empty fetch result',  async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve([]),
            })
        );

        const {container} = render(<FriendListPage/>)
        await new Promise(res => setTimeout(res, 2000));

        expect(container.querySelector("ul")).toBeInTheDocument()
        expect(container.querySelector("ul").children.length).toEqual(0)
    });

    it('render not empty friend list on non empty fetch result',  async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve([
                    {id: 1, name: "A", age: 1},
                    {id: 2, name: "B", age: 2},
                    {id: 3, name: "C", age: 3}
                ]),
            })
        );


        const {container} = render(<FriendListPage/>)
        await new Promise(res => setTimeout(res, 2000));

        let ul = container.querySelector("ul");
        expect(ul).toBeInTheDocument()
        expect(ul.children.length).toEqual(3)
        expect(ul.children[0].textContent).toEqual("A")
        expect(ul.children[1].textContent).toEqual("B")
        expect(ul.children[2].textContent).toEqual("C")
    });
})