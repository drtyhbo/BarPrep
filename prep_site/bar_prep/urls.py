from django.conf.urls.defaults import *

urlpatterns = patterns('prep_site.bar_prep.views',
    (r'^questions/$', 'questions_view'),
    (r'^xml/answers/$', 'xml_answers_view'),
    (r'^xml/questions/$', 'xml_questions_view'),
)
