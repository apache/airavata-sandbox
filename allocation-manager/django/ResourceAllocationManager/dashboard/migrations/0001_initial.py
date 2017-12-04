# -*- coding: utf-8 -*-
# Generated by Django 1.10 on 2017-12-01 04:20
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Request',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('request_title', models.CharField(max_length=250)),
                ('request_description', models.CharField(max_length=500)),
                ('request_status', models.CharField(max_length=20)),
                ('request_comments', models.FileField(upload_to=b'')),
                ('allocation_type', models.CharField(max_length=100)),
                ('cpu_hours_requested', models.CharField(max_length=10)),
                ('cpu_hours_allocated', models.CharField(max_length=10)),
            ],
        ),
    ]