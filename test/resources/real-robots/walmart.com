#Sitemaps

Sitemap: http://www.walmart.com/sitemap_cp.xml
Sitemap: http://www.walmart.com/sitemap_bp.xml
Sitemap: http://www.walmart.com/sitemap_ip.xml
Sitemap: http://www.walmart.com/sitemap_tp.xml
# Disallow the following URLs
User-agent: *
Disallow: /solutions/
Disallow: /cart2/
Disallow: /cservice/
Disallow: /search/search-ng.do?
Disallow: /catalog/
Disallow: /wmflows/

