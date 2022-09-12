from django.urls import path

from . import views

urlpatterns = [
    path('molecule/', views.client),
]
