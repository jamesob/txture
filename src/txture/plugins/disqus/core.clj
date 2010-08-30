(ns txture.plugins.disqus.core)

(def *disqus-short-name* nil)

;; only uncomment once supplying a Disqus short name for your site.
(def disqus-box
  (if (not (nil? *disqus-short-name*))
    (str "
         <div id='disqus_thread'></div>
         <script type='text/javascript'>
         /**
         * var disqus_identifier; [Optional but recommended: Define a unique identifier (e.g. post id or slug) for this thread] 
         */
         var disqus_developer = 1;
         (function() {
         var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
         dsq.src = 'http://" 
         *disqus-short-name* 
         ".disqus.com/embed.js';
         (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
         })();
         </script>
         <noscript>Please enable JavaScript to view the <a href='http://disqus.com/?ref_noscript="
         *disqus-short-name* 
         "'>comments powered by Disqus.</a></noscript>")
    ""))

(defn wrap-post-HTML
  "Add javascript imports to head."
  [html]
  (if (not (nil? *disqus-short-name*))
    [:div.plus-comment html disqus-box]
    html))

(defn append-to-body-end
  []
  (if (not (nil? *disqus-short-name*))
    (str "
         <script type='text/javascript'>
         var disqus_shortname = '" 
         *disqus-short-name* 
         "';
         (function () {
         var s = document.createElement('script'); s.async = true;
         s.src = 'http://disqus.com/forums/" 
         *disqus-short-name* 
         "/count.js';
         (document.getElementsByTagName('HEAD')[0] || document.getElementsByTagName('BODY')[0]).appendChild(s);
         }());
         </script>
         ")
    ""))

