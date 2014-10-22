<style type="text/css">
body {
    font: 16px sans-serif;
    margin: 0;
    padding: 0;
}
.content {
    background: white;
    margin: 0 auto;
    padding: 15px 30px 15px 30px;
    width: 800px;
}
.question {
    margin-bottom: 20px;
}
.explanation {
    font-style: italic;
    margin-bottom: 30px;
}
</style>

<div class="content">
    {% for item in question_list %}
        <div class="question">
            <div>{{ item.question.question }}</div>
            <ol type="a">
                {% for answer in item.answers %}
                <li>{{ answer.answer }}</li>
                {% endfor %}
            </ol>
            <div class="explanation">{{ item.question.explanation }}</div>
        </div>
    {% endfor %}
</div>
