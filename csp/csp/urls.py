from django.conf.urls import patterns, include, url
from django.contrib import admin

from rest_framework import routers
from csa import views

router = routers.DefaultRouter()
router.register(r'users', views.UserViewSet)
router.register(r'groups', views.GroupViewSet)
# router.register(r'notifications', views.NotificationViewSet)

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'csp.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),

    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^notification/$', views.NotificationList.as_view(), name='notification-list'),
    url(r'^notification/(?P<latitude>[\d\.]+)/(?P<longitude>[\d\.]+)/$', views.NotificationInArea.as_view(), name='notofication-area'),
    url(r'^notification/(?P<id>\d+)/rate/positive/$', views.vote_positive),
    url(r'^notification/(?P<id>\d+)/rate/negative/$', views.vote_negative),
    url(r'^docs/', include('rest_framework_swagger.urls')),
    url(r'^rest-api/', include('rest_framework_docs.urls')),
)
