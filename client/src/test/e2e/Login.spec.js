import {Browser, chromium, firefox, Page} from "playwright";

let chromiumBrowser: Browser;
let firefoxBrowser: Browser;
beforeAll(async () => {
    chromiumBrowser = await chromium.launch();
    firefoxBrowser = await firefox.launch();
});
afterAll(async () => {
    await chromiumBrowser.close();
    await firefoxBrowser.close()
});

let chromePage: Page;
let firefoxPage: Page;
beforeEach(async () => {
    chromePage = await chromiumBrowser.newPage();
    firefoxPage = await firefoxBrowser.newPage();
});
afterEach(async () => {
    await chromePage.close();
    await firefoxPage.close();
});

describe("RegisterAndLoginPage tests", () => {
    it("LoginPage allows you to login", async () => {
        await chromePage.goto("http://localhost:3000/");
        await chromePage.locator(".loginTabLink").evaluate(e => e.click())
        await chromePage.locator("input[name=login]").fill("First")
        await chromePage.locator("input[name=password]").fill("1")
        await chromePage.click("input[type=submit]")

        await chromePage.waitForSelector(".userName")

        await expect(chromePage.url()).toEqual("http://localhost:3000/");
        await expect(await chromePage.locator(".userName").innerText()).toEqual("First")
    });

    it("LoginPage allows see friend list", async () => {
        await chromePage.goto("http://localhost:3000/");
        await chromePage.locator(".loginTabLink").evaluate(e => e.click())
        await chromePage.locator("input[name=login]").fill("First")
        await chromePage.locator("input[name=password]").fill("1")
        await chromePage.click("input[type=submit]")

        await chromePage.waitForSelector(".userName")

        const friends = await chromePage.locator("ul[class=friendList]").allInnerTexts()

        await expect(chromePage.url()).toEqual("http://localhost:3000/");
        await expect(friends.join(" ")).toContain("Second")
    });

    it("LoginPage allows see chat list", async () => {
        await chromePage.goto("http://localhost:3000/");
        await chromePage.locator(".loginTabLink").evaluate(e => e.click())
        await chromePage.locator("input[name=login]").fill("First")
        await chromePage.locator("input[name=password]").fill("1")
        await chromePage.click("input[type=submit]")

        await chromePage.waitForSelector(".userName")

        await chromePage.goto("http://localhost:3000/friend?id=52")

        await chromePage.waitForSelector(".message")

        const messages = await chromePage.locator(".message").allInnerTexts()

        await expect(messages.join(" ")).toContain(
            "Hi! ? Sup! How u doing? Superb U?"
        )
    });

    it("LoginPage allows chat between", async () => {
        await chromePage.goto("http://localhost:3000/");
        await chromePage.locator(".loginTabLink").evaluate(e => e.click())
        await chromePage.locator("input[name=login]").fill("First")
        await chromePage.locator("input[name=password]").fill("1")
        await chromePage.click("input[type=submit]")

        await chromePage.waitForSelector(".userName")

        await chromePage.goto("http://localhost:3000/friend?id=52")
        await chromePage.locator("input[name=message]").fill("HeeeeeeeeeeeeeLoooooooo")
        await chromePage.click("input[type=submit]")

        await firefoxPage.goto("http://localhost:3000/");
        await firefoxPage.locator(".loginTabLink").evaluate(e => e.click())
        await firefoxPage.locator("input[name=login]").fill("Second")
        await firefoxPage.locator("input[name=password]").fill("2")
        await firefoxPage.click("input[type=submit]")

        await firefoxPage.waitForSelector(".userName")

        await firefoxPage.goto("http://localhost:3000/friend?id=51")
        await firefoxPage.waitForSelector(".message")
        await expect((await firefoxPage.locator(".message").allInnerTexts()).join(" "))
            .toContain("HeeeeeeeeeeeeeLoooooooo")

        await firefoxPage.locator("input[name=message]").fill("LooooooooHeeeeeeeeeeeee")
        await firefoxPage.click("input[type=submit]")

        await chromePage.goto("http://localhost:3000/friend?id=52")
        await chromePage.waitForSelector(".message")
        await expect((await chromePage.locator(".message").allInnerTexts()).join(" "))
            .toContain("LooooooooHeeeeeeeeeeeee")
    });
})