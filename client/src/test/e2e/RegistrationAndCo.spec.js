import {Browser, chromium, Page} from "playwright";

let browser: Browser;
beforeAll(async () => {
    browser = await chromium.launch();
});
afterAll(async () => {
    await browser.close();
});

let page: Page;
beforeEach(async () => {
    page = await browser.newPage();
});
afterEach(async () => {
    await page.close();
});

describe("RegisterAndLoginPage tests", () => {
    it("RegisterPage allows you to register", async () => {
        await page.goto("http://localhost:3000/");
        await page.locator(".registrationTabLink").evaluate(e => e.click())
        await page.locator("input[name=login]").fill("login")
        await page.locator("input[name=age]").fill("1")
        await page.locator("input[name=password]").fill("123")
        await page.click("input[type=submit]")

        await page.waitForSelector(".userName")

        await expect(page.url()).toEqual("http://localhost:3000/");
        await expect(await page.locator(".userName").innerText()).toEqual("login")
    });

    it("LoginPage allows you to login", async () => {
        await page.goto("http://localhost:3000/");
        await page.locator(".loginTabLink").evaluate(e => e.click())
        await page.locator("input[name=login]").fill("login")
        await page.locator("input[name=password]").fill("123")
        await page.click("input[type=submit]")

        await page.waitForSelector(".userName")

        await expect(page.url()).toEqual("http://localhost:3000/");
        await expect(await page.locator(".userName").innerText()).toEqual("login")
    });

    it("LoginPage allows see friend list", async () => {
        await page.goto("http://localhost:3000/");
        await page.locator(".loginTabLink").evaluate(e => e.click())
        await page.locator("input[name=login]").fill("login")
        await page.locator("input[name=password]").fill("123")
        await page.click("input[type=submit]")

        await page.waitForSelector(".userName")

        const friends = await page.locator("ul[class=friendList]").allInnerTexts()

        await expect(page.url()).toEqual("http://localhost:3000/");
        await expect(friends.join(" ")).toContain("First")
        await expect(friends.join(" ")).toContain("Second")
    });

    it("LoginPage allows see chat list", async () => {
        await page.goto("http://localhost:3000/");
        await page.locator(".loginTabLink").evaluate(e => e.click())
        await page.locator("input[name=login]").fill("First")
        await page.locator("input[name=password]").fill("1")
        await page.click("input[type=submit]")

        await page.waitForSelector(".userName")

        await page.goto("http://localhost:3000/friend?id=52")

        await page.waitForSelector(".message")

        const messages = await page.locator(".message").allInnerTexts()

        await expect(messages.join(" ")).toEqual(
            "Hi! ? Sup! How u doing? Superb U?"
        )
    });
})