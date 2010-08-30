# txture: a web log engine for people who like flat files, convenience, and Lisp

## That fresh-ink scent don't come off a relational database.

I like flat files better than databases for my writing. I also like
``meta`` tags and other search-engine luring information to be derived and
embedded into HTML based on the blog entries I write.

**txture** is based on these priorities. **txture** is readable and easily modified,
but it (should) work fine out of the box.

## Philosophy

Txture boasts a bare core reliant on a simple, powerful plug-in architecture for
all modern goodies.

Txture is shaped according to the Unix philosophy as nearly as a web log
platform can sanely afford. Comments are probably handled best by Disqus.
Existing Markdown processors written in JavaScript should be used instead of
something I'd write from scratch in Clojure. CSS rendering tools like Less
should be used via existing, separate scripts. The filesystem should be used for
storing post information: you wanna symlink your posts in from somewhere else?
wanna use git to version control your writing? wanna do both? I don't give a
goddamn and I shouldn't have to: txture's use of flatfiles allows you to do
whatever you like, so long as you specify the location and file-type of the
posts you're delivering.

txture offers a very minimal core which can be extended and manipulated by a
robust plug-in architecture. A great plug-in architecture will allow you to 

    1. easily extend txture to accommodate your likes, and
    2. allow you to easily distribute your plug-in to others.
    These two consequences will facilitate txture's longevity and adaptivity.

Txture's design philosophy is as follows:

    1. never duplicate functionality that other, better, and existing tools
       already provide. 
    2. anything that is not basic to txture is a plugin. This includes features
       like Markdown parsing, CSS rendering, Disqus comments which are included
       within the core of other minimal weblog platforms. This reliance on a
       plug-in architecture will not only keep the txture core stark, but it
       will provide numerous examples of how plug=ins are authored.

## Note

The current `lein` script causes step 4 of the installation to barf, so I've
included the version of `lein` I use in the repository.

## Usage

1. Read `src/txture/config.clj`. Modify as necessary.
2. `mkdir` the directory where you'll be storing your posts.
3. `cd` into posts directory, crack open your favorite text editor and type up a post 
   in the following form (YAML):

        title: Yabba dabba doo
        subtitle: A stern treatise on the Red Scare
        labels: fake, phony, lame

        [Markdown here]
   
   If you don't like Markdown, the Markdown plug-in can be disabled simply by
   removing the `src/txture/plugins/markdown` file.

   Ensure that there is a blank line between the header information and the
   body, which can be any HTML.

## Installation

1. Get Leiningen (Clojure not required)
2. Get txture

        $ git clone git@github.com:jamesob/txture.git

3. Let ``lein`` gather dependencies.

        $ cd txture && lein deps

4. Run txture

        $ lein repl src/txture/core.clj

5. Browse to http://localhost:8080 

## License

Copyright (c) 2010 jamesob

     Permission is hereby granted, free of charge, to any person obtaining a copy
     of this software and associated documentation files (the "Software"), to deal
     in the Software without restriction, including without limitation the rights
     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     copies of the Software, and to permit persons to whom the Software is
     furnished to do so, subject to the following conditions:

     The above copyright notice and this permission notice shall be included in
     all copies or substantial portions of the Software.

     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
     THE SOFTWARE.

## TODO

  * More bland default theme.
  * Cleaner barfing.
  * Markdown parsing.
  * Plugins?
    * Comments
      * Disqus?
    * Twitter integration
    * Delicious
  * Non-blog pages?
  * Pretty printing source.

