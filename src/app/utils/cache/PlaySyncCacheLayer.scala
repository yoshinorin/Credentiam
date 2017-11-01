package utils.cache

import java.util.UUID
import javax.inject.{ Inject, Singleton }
import scala.concurrent.duration.{ Duration, _ }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.reflect.ClassTag
import play.api.cache._
import play.api.cache.ehcache.EhCacheApi
import net.sf.ehcache.config.CacheConfiguration
import net.sf.ehcache.Cache
import net.sf.ehcache.CacheManager

object PlaySyncCacheLayer {

  val cacheName = "defaultsynccache"
  val cacheManager = CacheManager.create
  cacheManager.addCache(cacheName)

  val cache: PlaySyncCacheLayer = new PlaySyncCacheLayer(new DefaultSyncCacheApi(new EhCacheApi(cacheManager.getEhcache(cacheName))))

}

@Singleton
class PlaySyncCacheLayer @Inject() (cache: DefaultSyncCacheApi) {

  def get[T](key: String)(implicit cTag: ClassTag[T]): Option[T] = {
    cache.get[T](key)
  }

  def set(key: String, value: Any, dur: Duration = Duration.Inf): Unit = {
    cache.set(key, value, dur)
  }

  def remove(key: String): Unit = {
    cache.remove(key)
  }
}
