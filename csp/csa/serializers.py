__author__ = 'hubert'
from rest_framework import serializers
from csa.models import *


class NotificationSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Notification
        fields = ('id','created_at', 'latitude', 'longitude', 'kind','text_note','confirmed','debunk',)