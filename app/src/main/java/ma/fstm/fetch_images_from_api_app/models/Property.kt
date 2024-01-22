package ma.fstm.fetch_images_from_api_app.models
data class Property(val id: Int, val title: String = "", val description: String = "", val image: String = "", val horizontal: Boolean = false, val data: List<Property>? = null, var selected: Boolean? = false)