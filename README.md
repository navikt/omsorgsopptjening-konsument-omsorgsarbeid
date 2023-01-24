# omsorgsopptjening-konsument-omsorgsarbeid
Henter utført omsorgsarbeid/barnetrygd fra kafka topic ```omsorgsarbeid-mock-topic```, og legger deretter innholdet
på ```omsorgsopptjening```-topic.

Formålet med appen er å fungere som et anti corruption layer mot den eksterne topicen ```omsorgsarbeid-mock-topic``` og
den interne topicen  ```omsorgsopptjening```.
Appen setter også i gang videre prossessering av omsorgsopptjening.