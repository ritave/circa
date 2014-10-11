from django.http.response import HttpResponse
from django.shortcuts import render
from csa.models import *

# Create your views here.
from django.contrib.auth.models import User, Group
from django.views.decorators.csrf import csrf_exempt
from rest_framework import viewsets
from rest_framework.permissions import AllowAny
from csa.serializers import *

from rest_framework import generics


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = (AllowAny,)


class GroupViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    queryset = Group.objects.all()
    serializer_class = GroupSerializer
    permission_classes = (AllowAny,)


class NotificationList(generics.ListCreateAPIView):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    serializer_class = NotificationSerializer

    # def get_queryset(self):
    #     queryset = Notification.getObjectsInRange(0,0)
    #     return queryset
    # queryset = Notification.objects.all()
    model = Notification
    paginate_by = None

    permission_classes = (AllowAny,)




class NotificationInArea(generics.ListAPIView):
    serializer_class = NotificationSerializer
    permission_classes = (AllowAny,)
    paginate_by = None

    def get_queryset(self):
        latitude = float(self.kwargs['latitude'])
        longitude = float(self.kwargs['longitude'])
        queryset = Notification.getObjectsInRange(latitude,longitude)
        return queryset

@csrf_exempt
def vote_positive(request, id):
    if request.method == "POST":
        Notification.objects.get(id=id).addPositiveVote()
        return HttpResponse("ok")
    return HttpResponse("invalid method")

@csrf_exempt
def vote_negative(request, id):
    if request.method == "POST":
        Notification.objects.get(id=id).addNegativeVote()
        rating = Notification.objects.get(id=id).positive - Notification.objects.get(id=id).negative
        if (rating < -1):
            Notification.objects.get(id=id).delete()
        return HttpResponse("ok")
    return HttpResponse("invalid method")