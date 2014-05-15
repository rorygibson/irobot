# /robots.txt
Sitemap: http://sitemap.target.com/wcsstore/SiteMap/sitemap_index.xml.gz
Sitemap: http://tgtfiles.target.com/sitemaps/sitemap_assorted_index.xml.gz
Sitemap: http://sitemap.target.com/wcsstore/SiteMap/sitemap_image_index.xml.gz
Sitemap: http://sitemap.target.com/wcsstore/SiteMap/sitemap_video_index.xml.gz
 
User-agent: *
Disallow: /OtherDisplayView
Disallow: /HelpContent
Disallow: /QuickInfoView
Disallow: /AddToList
Disallow: /AddToRegistry
Disallow: /AjaxSearchNavigationView
Disallow: /SearchNavigationView
Disallow: /Checkout
Disallow: /CheckoutEditItemsDisplayView
Disallow: /CheckoutOrderBillingView
Disallow: /CheckoutOrderShippingView
Disallow: /CheckoutSignInView
Disallow: /ExitCheckoutCmd
Disallow: /EmailCartView
Disallow: /ESPDisplayOptionsViewCmd
Disallow: /FetchProdRefreshContent
Disallow: /FiatsCmd
Disallow: /GenericRegistryPortalView
Disallow: /guestEmailNotificationView
Disallow: /GuestAsAnonymous
Disallow: /LogonForm
Disallow: /ManageOrder
Disallow: /ManageReturns
Disallow: /MediaDisplayView
Disallow: /OrderItemDisplay
Disallow: /PhotoUpload
Disallow: /ProductComparisonCmd
Disallow: /PromotionDisplayView
Disallow: /PromotionDetailsDisplayView
Disallow: /RegistryPortalCmd
Disallow: /ReportAbuse
Disallow: /SingleShipmentOrderSummaryView
Disallow: /splitOrderItems
Disallow: /TargetListPortalView
Disallow: /TargetStoreLocatorCmd
Disallow: /WriteComments
Disallow: /WriteReviews
Disallow: /webapp
Disallow: /advancedGiftRegistrySearchView
Disallow: /SpecificationDefinitionView
Disallow: /VariationSelectionView
Disallow: /FeaturedShowMoreOverlay
Disallow: /c/activision/*Ntk-All/Ntt
Disallow: /c/adventure-cameras-camcorders-electronics/*Ntk-All/Ntt