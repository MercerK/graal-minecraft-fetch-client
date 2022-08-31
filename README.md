# FetchClient

This is a simple library implementation for Grakkit (or other GraalVM implementations within Minecraft) providing fetch functionality. 

# Grakkit - How to use

* Add library file to your grakkit folder, such as `./plugins/grakkit`
* Add the following:

```JavaScript
// Loads the jar
const FetchClient = core.load('./plugins/grakkit/fetchClient.jar', 'fetch.FetchClient') 

const client = new FetchClient()

// Adds the plugin; needed to fix async issues
client.setPlugin(core.plugin)
```

* Once declared, you can start using it.

```JavaScript
// Example
console.log((await client.fetch('https://api.publicapis.org/entries', 'GET', '', [])).body())
```

There are a total of four parameters (all required). 

* URL: This is the target URL.
* Method: This is 'GET', 'POST', etc.
* Payload: This is the string payload. For empty, you can do `''`.
* Headers: This accepts headers in an flat array. 
  * Example: `['Content-Type', 'application/json; charset=utf-8']`

# Build it yourself

* Clone repo locally.
* Run `./gradlew shadowJar`

# Resources

* [Discord](https://discord.gg/e682hwR)
* [Grakkit](https://github.com/Grakkit/grakkit)
* [CraftJS](https://github.com/Dysfold/craftjs): These guys deserve a huge shoutout! They are the originating authors of the fetch functionality. 