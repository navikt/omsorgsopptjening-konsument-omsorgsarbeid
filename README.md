# omsorgsopptjening-konsument-omsorgsarbeid
Henter utført omsorgsarbeid/barnetrygd fra kafka topic ```omsorgsarbeid-mock-topic```, og legger deretter innholdet på ```omsorgsopptjening```-topic.

Formålet med appen er å fungere som et anti corruption layer den eksterne topicen ```omsorgsarbeid-mock-topic``` og den interne topicen  ```omsorgsopptjening```.
Appen setter også videre prossessering av omsorgsopptjening