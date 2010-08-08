# txture: a web log engine for people who like flat files, convenience, and Lisp

**That fresh-ink scent don't come off a relational database.**

I like flat files better than databases for my writing. I also like
``meta`` tags and other search-engine luring information to be derived and
embedded into HTML based on the blog entries I write.

**txture** is based on these priorities. **txture** is readable and easily modified,
but it (should) work fine out of the box.

## Note

The current `lein` script causes step 4 of the installation to barf, so I've
included the version of `lein` I use in the repository.

## Usage

1. Check out `src/txture/config.clj`. Modify as necessary.
2. `mkdir` the directory where you'll be storing your posts.
3. `cd` into posts directory, crack open your favorite text editor and type up a post 
   in the following form:

        :title: Yabba dabba doo
        :subtitle: A stern treatise on the Red Scare
        :labels: fake, phony, lame

        [HTML here]
   
   Ensure that there is a blank line between the header information and the
   body, which can be any HTML.

4. Realize that, oh my god, I shouldn't be using this: it's so primitive right
now.

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

