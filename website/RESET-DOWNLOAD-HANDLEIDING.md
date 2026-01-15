# Reset download (support)

Deze handleiding is voor support wanneer een betaalde klant na herinstallatie opnieuw moet kunnen unlocken.

## Vereisten

- In Vercel is `SUPPORT_RESET_TOKEN` ingesteld.

## Reset uitvoeren via API

**Endpoint**

```
POST https://www.seniorease.eu/api/reset-download
```

**Headers**

```
Content-Type: application/json
X-Support-Token: <jouw token>
```

**Body (op basis van e-mail)**

```
{
  "email": "klant@email.com"
}
```

**Body (als je de payment intent hebt)**

```
{
  "payment_intent": "pi_..."
}
```

## Verwachte response

- Succes: `{ "reset": true }`
- Geen match of fout: `{ "reset": false }`

## Tips

- Gebruik bij voorkeur `payment_intent` als die beschikbaar is.
- Als `reset: false` terugkomt, controleer of:
  - het juiste e‑mailadres is gebruikt
  - er een betaalde Stripe‑betaling bestaat voor dit e‑mailadres
  - de klant het juiste product heeft gekocht
