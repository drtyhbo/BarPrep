from django.conf.urls.defaults import *
from django.contrib import admin

import bar_prep.urls

admin.autodiscover()

urlpatterns = patterns('',
    # Example:
    (r'', include(bar_prep.urls)),

    # Uncomment the admin/doc line below and add 'django.contrib.admindocs' 
    # to INSTALLED_APPS to enable admin documentation:
    # (r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    (r'^admin/', include(admin.site.urls)),
)
