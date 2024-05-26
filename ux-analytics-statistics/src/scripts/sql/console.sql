select uj.token, uj.email, uj.started_at, uj.ended_at, uj.form_number, ujc.name, ujc.unit, ujc.value
from user_journey_characteristic ujc
         left join user_journey uj on ujc.user_journey_id = uj.id;