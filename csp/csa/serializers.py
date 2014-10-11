__author__ = 'hubert'
from django.contrib.auth.models import User, Group
from rest_framework import serializers
from csa.models import Notification

class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('url', 'username', 'email', 'groups')

class GroupSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Group
        fields = ('url', 'name')

class NotificationSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Notification
        fields = ('id','latitude','longitude','checkin_type','other_description','positive','negative', 'date',)