from django.db import models

class Category(models.Model):
    name = models.CharField(max_length=64)

    def __unicode__(self):
        return self.name

class Question(models.Model):
    question = models.TextField()
    explanation = models.TextField()
    category = models.ForeignKey(Category)

    def __unicode__(self):
        return self.question

class Answer(models.Model):
    INDEX_CHOICES = (
        (0, 'A'),
        (1, 'B'),
        (2, 'C'),
        (3, 'D'),
        (4, 'E')
    )
    answer = models.TextField()
    question = models.ForeignKey(Question)
    answer_index = models.IntegerField(choices=INDEX_CHOICES)
    is_correct = models.BooleanField(default=False)

    def __unicode__(self):
        return '%s: %s' % (self.INDEX_CHOICES[self.answer_index][1], self.answer)

