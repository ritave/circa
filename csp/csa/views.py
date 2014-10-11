from django.shortcuts import render
from csa.models import *

# Create your views here.
from django.contrib.auth.models import User, Group
from rest_framework import viewsets
from csa.serializers import *


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all()
    serializer_class = UserSerializer


class GroupViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    queryset = Group.objects.all()
    serializer_class = GroupSerializer


class NotificationViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    serializer_class = NotificationSerializer

    def get_queryset(self):
        queryset = Notification.getObjectsInRange(0,0)
        return queryset