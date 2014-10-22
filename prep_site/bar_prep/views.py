from django.core import serializers;
from django.http import HttpResponse
from django.views.generic.simple import direct_to_template

from models import Question, Answer

def questions_view(request):
    question_list = []

    questions = Question.objects.all()
    for question in questions:
        answers = Answer.objects.filter(question=question).order_by('answer_index')
        question_list.append({
            'question': question,
            'answers': answers
        })
    
    return direct_to_template(request, 'questions.tpl', {
        'question_list': question_list
    })

def xml_answers_view(request):
    data = ''
    for answer in Answer.objects.all():
        data += model_to_xml(answer, ['id', 'answer', 'question_id',
            'answer_index', 'is_correct'])
    data = '<?xml version="1.0" encoding="utf-8"?><models>%s</models>' % data
    return HttpResponse(data, mimetype='text/xml')

def xml_questions_view(request):
    data = ''
    for question in Question.objects.all():
        data += model_to_xml(question, ['id', 'question', 'explanation'])
    data = '<?xml version="1.0" encoding="utf-8"?><models>%s</models>' % data
    return HttpResponse(data, mimetype='text/xml')

def model_to_xml(model, fields):
    data = ''
    for field in fields:
        data += '<%s><![CDATA[%s]]></%s>' % (field, model.__dict__[field], field)
    return '<model>%s</model>' % data
