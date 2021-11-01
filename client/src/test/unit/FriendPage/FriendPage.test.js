import {render, screen, waitFor} from "@testing-library/react";
import FriendPage from "../../../pages/FriendPage/FriendPage";

describe("<FriendPage/>", () => {
    it('render friend name, age and id', async function () {
        jest.spyOn(URLSearchParams.prototype, 'get').mockImplementation(() => 1);

        global.fetch = jest.fn((args) => {
                if (args.includes("/friend")) {
                    return Promise.resolve({
                        json: () => Promise.resolve({id: 1, name: "A", age: 2}),
                    })
                }

                if (args.includes("/chat")) {
                    return Promise.resolve({
                        json: () => Promise.resolve([]),
                    })
                }
            }
        );

        render(<FriendPage/>)

        await waitFor(() => screen.getByText("A#1"))
        expect(screen.getByText("A#1")).toBeInTheDocument()
        expect(screen.getByText("age: 2")).toBeInTheDocument()
    });

    it('render nothing with no friend received', async function () {
        jest.spyOn(URLSearchParams.prototype, 'get').mockImplementation(() => 1);

        global.fetch = jest.fn((args) => {
                if (args.includes("/friend")) {
                    return Promise.resolve({
                        json: () => Promise.resolve({}),
                    })
                }

                if (args.includes("/chat")) {
                    return Promise.resolve({
                        json: () => Promise.resolve([]),
                    })
                }
            }
        );

        render(<FriendPage/>)

        expect(screen.getByText("#")).toBeInTheDocument()
        expect(screen.getByText("age:")).toBeInTheDocument()
    });

    it('render chat', async function () {
        jest.spyOn(URLSearchParams.prototype, 'get').mockImplementation(() => 1);

        global.fetch = jest.fn((args) => {
                if (args.includes("/friend")) {
                    return Promise.resolve({
                        json: () => Promise.resolve({id: 1, name: "Name", age: 12}),
                    })
                }

                if (args.includes("/chat")) {
                    return Promise.resolve({
                        json: () => Promise.resolve([
                            {from: 1, to: 2, message: "aba"},
                            {from: 2, to: 1, message: "caba"},
                            {from: 1, to: 2, message: "ABACABA"},
                        ]),
                    })
                }
            }
        );

        render(<FriendPage/>)

        await waitFor(() => screen.getByText("Name#1"))
        expect(screen.getByText("aba")).toBeInTheDocument()
        expect(screen.getByText("caba")).toBeInTheDocument()
        expect(screen.getByText("ABACABA")).toBeInTheDocument()
    });
})