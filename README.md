# HanziHack

[![Build Status](https://travis-ci.com/hanzihack/hanzihack.svg?branch=master)](https://travis-ci.com/hanzihack/hanzihack)

The system to help you learn Chinese words.  
Implementation of [Marilyn Method][2]

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

## Configuration

The configuration can be defined in `.edn` file or as environment variables.

See all variables here `src/clj/hanzihack/config.clj`.

Example Configuration, create `dev-config.edn` at project root with this content
```
{:dev true
 :port 3000
 ;; when :nrepl-port is set the application starts the nREPL server on load
 :nrepl-port 7000
 
 ; set your dev database connection URL here
 :database-url "postgresql://localhost/hanzihack_dev?user=postgres&password="}
```


## Running

Run the database migrations, run:

    lein run migrate

Other supported commands are `reset`, `destroy`, `pending`, and `rollback`. See usage at [migratus][3]
    
    
To start a web server for the application, run:

    lein run 

To start CLJS web application, run:
    
    lein shadow watch app
    
    
## Development

To start Clojure REPL, run:

    lein repl



To start ClojureScript REPL, run:

    lein shadown watch app

And connect to REPL via `localhost:7002` then run `(shadow/repl :app)` to connect with shadow-cljs nREPL


More information see doc here [REPL Driven Development][4]



## Build

Build Clojure backend server and ClojureScript Web Frontend UI, run:

    lein uberjar


## License

Copyright © 2019

[1]: https://github.com/technomancy/leiningen
[2]: https://countryoftheblind.blogspot.com/2012/01/mnemonics-for-pronouncing-chinese.html
[3]: https://github.com/yogthos/migratus#usage
[4]: http://www.luminusweb.net/docs/repl.html
