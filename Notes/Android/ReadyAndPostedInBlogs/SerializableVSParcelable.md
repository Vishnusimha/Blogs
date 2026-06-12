# [Serializable VS Parcelable.md](SerializableVSParcelable.md)

**Serializable** and **Parcelable** are two interfaces the mainly uses of them is related to the passing of data.🚀👨‍💻

1. Serializable:
    - Part of standard Java.
    - Very easy to use, just label your class as ”Serializable” without any further methods to implement.
    - Slower than ”Parcelable”, because it uses reflection to serialize and deserialize your object.
    - Serializable objects are not thread-safe.
    - Best Practice: Good for storing data to files or sending over the network.

2. Parcelable:
    - Specific for android.
    - Very easy to use, you annotate your class as ”Parcelable”.
    - Faster and efficient than “Serializable”.
    - Parcelable objects are thread-safe.
    - Best Practice: Better for passing data within your app (like passing objects between activities or fragments).

💡When to use them:

- Choose “Serializable” if you want something simple and aren't worried about speed.
- Choose “Parcelable” when you need better speed and efficiency in your android app.
