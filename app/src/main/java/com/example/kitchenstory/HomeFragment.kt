package com.example.kitchenstory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kitchenstory.databinding.FragmentHomeBinding
import com.example.kitchenstory.databinding.FragmentSignInBinding
import com.facebook.stetho.inspector.protocol.module.Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), AddedIngredientsAdapter.AddedIngredientsAdapterInterface {
    private val TAG = "HomeFragment"
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var databaseRef: DatabaseReference
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: AddedIngredientsAdapter
    private lateinit var mList:MutableList<AddedIngredientsData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()
    }

    private fun init(view: View){
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Ingredients").child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = AddedIngredientsAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter

    }

    private fun registerEvents() {
        binding.todoNextBtn.setOnClickListener {
            val newIngredient = binding.ingredientET.text.toString()
            if(newIngredient.isNotEmpty()){
                onSaveIngredient(newIngredient)
            } else {
                Toast.makeText(context, "Please type in an ingredient", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDataFromFirebase(){
        databaseRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (ingSnapshot in snapshot.children){
                    val curr_ingredient = ingSnapshot.key?.let {
                        AddedIngredientsData(it, ingSnapshot.value.toString())
                    }
                    if (curr_ingredient != null){
                        mList.add(curr_ingredient)
                    }
                }
                Log.d(TAG, "onDataChange: " + mList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun onSaveIngredient(newIngredient: String) {
        databaseRef.push().setValue(newIngredient).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "Ingredient saved successfully!", Toast.LENGTH_SHORT).show()
                binding.ingredientET.text = null
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                Log.e("HomeFragment", it.exception.toString())
            }
        }
    }

    override fun onDeleteItemClicked(addedIngredientsData: AddedIngredientsData){
        databaseRef.child(addedIngredientsData.ingredientId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}