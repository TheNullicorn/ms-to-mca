### Package Overview

---

`http` - Abstraction layer over an HTTP client, which is used internally for requests sent to
Microsoft and Mojang servers.

`json` - Abstraction layer over JSON objects, arrays, encoding and decoding. This is used internally
to parse and interpret responses received from the `http` package.

`minecraft` A minimal implementation of a Minecraft authentication client. Only areas of the API
related to Xbox Live login are implemented.

`util` - Miscellaneous helpers that don't fit into any of the other packages.

`xbox` - A minimal implementation of an Xbox Live authentication client. Xbox Live is what allows us
to turn Microsoft access tokens into Mojang/Minecraft access tokens at the end of the process.