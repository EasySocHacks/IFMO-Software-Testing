import {render, screen, waitFor} from "@testing-library/react";
import Header from "../../../components/Header/Header";

describe("<Header/>", () => {
    it('render header friends tab', () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve({}),
            })
        );

        render(<Header/>);
        const friends = screen.getByText("Friends")
        expect(friends).toBeInTheDocument()
    });

    it("render header register and login tabs with no user present", () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve({}),
            })
        );

        render(<Header/>);
        expect(screen.getByText("Register")).toBeInTheDocument()
        expect(screen.getByText("Login")).toBeInTheDocument()
        expect(global.fetch).toBeCalledTimes(1)
    });

    it('render user name and logout tab on user present', async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve({id: 1, name: "ABAcaba", age: 1}),
            })
        );

        render(<Header/>);
        await waitFor(() => screen.getByText("ABAcaba"))

        expect(screen.getByText("ABAcaba")).toBeInTheDocument()
        expect(screen.getByText("Logout")).toBeInTheDocument()
        expect(global.fetch).toBeCalledTimes(1)
    });
})