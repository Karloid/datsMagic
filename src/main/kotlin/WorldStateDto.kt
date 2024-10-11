/**
 * {
 *   "anomalies": [
 *     {
 *       "effectiveRadius": 0,
 *       "id": "string",
 *       "radius": 0,
 *       "strength": 0,
 *       "velocity": {
 *         "x": 1.2,
 *         "y": 1.2
 *       },
 *       "x": 1,
 *       "y": 1
 *     }
 *   ],
 *   "attackCooldownMs": 1000,
 *   "attackDamage": 10,
 *   "attackExplosionRadius": 10,
 *   "attackRange": 10,
 *   "bounties": [
 *     {
 *       "points": 100,
 *       "radius": 10,
 *       "x": 1,
 *       "y": 1
 *     }
 *   ],
 *   "enemies": [
 *     {
 *       "health": 100,
 *       "killBounty": 10,
 *       "shieldLeftMs": 5000,
 *       "status": "alive",
 *       "velocity": {
 *         "x": 1.2,
 *         "y": 1.2
 *       },
 *       "x": 1,
 *       "y": 1
 *     }
 *   ],
 *   "mapSize": {
 *     "x": 1,
 *     "y": 1
 *   },
 *   "maxAccel": 1,
 *   "maxSpeed": 10,
 *   "name": "player1",
 *   "points": 100,
 *   "reviveTimeoutSec": 2,
 *   "shieldCooldownMs": 10000,
 *   "shieldTimeMs": 5000,
 *   "transportRadius": 5,
 *   "transports": [
 *     {
 *       "anomalyAcceleration": {
 *         "x": 1.2,
 *         "y": 1.2
 *       },
 *       "attackCooldownMs": 0,
 *       "deathCount": 0,
 *       "health": 100,
 *       "id": "00000000-0000-0000-0000-000000000000",
 *       "selfAcceleration": {
 *         "x": 1.2,
 *         "y": 1.2
 *       },
 *       "shieldCooldownMs": 0,
 *       "shieldLeftMs": 0,
 *       "status": "alive",
 *       "velocity": {
 *         "x": 1.2,
 *         "y": 1.2
 *       },
 *       "x": 1,
 *       "y": 1
 *     }
 *   ],
 *   "wantedList": [
 *     {
 *       "health": 100,
 *       "killBounty": 10,
 *       "shieldLeftMs": 5000,
 *       "status": "alive",
 *       "velocity": {
 *         "x": 1.2,
 *         "y": 1.2
 *       },
 *       "x": 1,
 *       "y": 1
 *     }
 *   ]
 * }
 */


class WorldStateDto(
    val anomalies: List<AnomalyDto>,
    val attackCooldownMs: Int,
    val attackDamage: Int,
    val attackExplosionRadius: Int,
    val attackRange: Int,
    val bounties: List<BountyDto>,
    val enemies: List<EnemyDto>,
    val mapSize: Point2DF,
    val maxAccel: Double,
    val maxSpeed: Double,
    val name: String,
    val points: Int,
    val reviveTimeoutSec: Int,
    val shieldCooldownMs: Int,
    val shieldTimeMs: Int,
    val transportRadius: Int,
    val transports: List<TransportDto>,
    val wantedList: List<EnemyDto>
)

class TransportDto(
    val anomalyAcceleration: Point2DF,
    val attackCooldownMs: Int,
    val deathCount: Int,
    val health: Int,
    val id: String,
    val selfAcceleration: Point2DF,
    val shieldCooldownMs: Int,
    val shieldLeftMs: Int,
    val status: String,
    val velocity: Point2DF,
    val x: Double,
    val y: Double
) {
    private var _pos: Point2DF? = null
    val pos: Point2DF
        get() = _pos ?: Point2DF(x, y).also { _pos = it }
}

data class AnomalyDto(
    val effectiveRadius: Double,
    val id: String,
    val radius: Double,
    val strength: Double,
    val velocity: Point2DF,
    val x: Double,
    val y: Double
)   {
    private var _pos: Point2DF? = null
    val pos: Point2DF
        get() = _pos ?: Point2DF(x, y).also { _pos = it }
}

data class BountyDto(
    val points: Double,
    val radius: Double,
    val x: Double,
    val y: Double
) {
    var _pos : Point2DF? = null
    val pos: Point2DF
        get() = _pos ?: Point2DF(x, y).also { _pos = it }
}

data class EnemyDto(
    val health: Int,
    val killBounty: Double,
    val shieldLeftMs: Double,
    val status: String,
    val velocity: Point2DF,
    val x: Double,
    val y: Double
) {
    private var _pos: Point2DF? = null
    val pos: Point2DF
        get() = _pos ?: Point2DF(x, y).also { _pos = it }
}


data class TransportsDto(
    val transports: List<TransportCommandDto>
)

data class TransportCommandDto(
    val acceleration: Point2DF,
    val activateShield: Boolean,
    val attack: Point2D?,
    val id: String
) {
}