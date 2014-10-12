from django.conf.urls import patterns, include, url
from django.contrib import admin

from rest_framework import routers
from csa import views

urlpatterns = patterns('',
    url(r'^admin/', include(admin.site.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^notification/$', views.NotificationList.as_view(), name='notification-list'),
    url(r'^notification/(?P<latitude>-?(\d*\.)?\d+)/(?P<longitude>-?(\d*\.)?\d+)/$',\
        views.NotificationInArea.as_view(), name='notofication-area'),
    url(r'^notification/(?P<id>\d+)/rate/positive/$', views.vote_positive,\
        name='notification-upvote'),
    url(r'^notification/(?P<id>\d+)/rate/negative/$', views.vote_negative,\
        name='notification-downvote'),
)
