# play-clj-nrepl

If you want to edit a running game from [play-clj](https://github.com/oakes/play-clj) within emacs, you're probably going to want to start an nrepl server. But if you make changes to ```:on-show```, they won't be run.

This small library makes it easy to start an nrepl server, and whenever you recompile, it'll reload your screen(s).

## Usage

Add the following dependency to your ```project.clj```:
```clojure [play-clj-nrepl "0.1.0" :exclusions [play-clj]]```

start an nrepl server when your game starts up:
```clojure 
(defgame your-game
  :on-create
  (ƒ [this]
    (nr/start-nrepl your-game 2000)
    (set-screen! this main-screen)))
```

This will start an nrepl server on port 2000. If you attach to this nrepl and recompile, it will reload (that is, call ```:on-show``` on) your screens automatically.

## License

Copyright © 2016 Bryce Covert

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
