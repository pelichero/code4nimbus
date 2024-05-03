import React from 'react'
import { Button } from "./components/ui/button";
import { Input } from "./components/ui/input";
import { Search, PlusCircle } from "lucide-react"
import { useNavigate } from "react-router-dom";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "./components/ui/table";
import { Dialog, DialogClose, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "./components/ui/dialog";
import { Label } from "./components/ui/label";
import { useEffect, useState } from "react";
import axios from 'axios'

type Repository = {
  id: number
  name: string
  slug: string
  price: number
}

export function App() {

  const [products, setRepositories] = useState<Repository[]>([])
  const initialValue = {
    name: '',
    slug: '',
    price: 0
  }

  useEffect(() => {
    axios.get('http://localhost:9000/products' , 
    {
      headers: {
          'Content-Type': 'application/json'
      }
    })
      .then(response => setRepositories(response.data))
  } , [])

  const [formData, setFormData] = useState(initialValue);
  //const navigate = useNavigate();

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    
    setFormData((prevState) => ({ 
      ...prevState, 
      [e.target.id]: e.target.value 
    }));

  }

  function onSubmit(ev: React.FormEvent<HTMLInputElement>){

    ev.preventDefault();

    axios.post('http://localhost:9000/product', formData , 
    {
      headers: {
          'Content-Type': 'application/json'
      }
    })
    .then(response => {
      console.log(response.data)
    })
    .catch(error => console.log(error));
  }
  return (

    <div className="p-6 max-w-4xl mx-auto space-y-4">

      <h1 className="text-3xl font-bold">Products</h1>

      <div className="flex item-center justify-between">
        <form className="flex item-center gap-2">
            <Input name="id" placeholder="Product id"/>
            <Input name="name" placeholder="Product name"/>
            <Button type="submit" variant="link">
              <Search className="w-4 h-3 mr-2" />
              Filter results
            </Button>
        </form>

        <Dialog>
          <DialogTrigger asChild>
            <Button> 
              <PlusCircle className="w-4 h-3 mr-2" />
              New product
            </Button>
          </DialogTrigger>

          <DialogContent>
            <DialogHeader>
              <DialogTitle>New product</DialogTitle>
              <DialogDescription>Fill the form below to create a new product</DialogDescription>
            </DialogHeader>
            <form className="space-y-6" onSubmit={onSubmit}>
              <div className="grid grid-cols-6 items-center text-right gap-3">
                <Label htmlFor="name">Product</Label>
                <Input className="col-span-3" placeholder="Product name" id="name" onChange={onChange}/>
              </div>
              <div className="grid grid-cols-6 items-center text-right gap-3">
                <Label htmlFor="price">Price</Label>
                <Input className="col-span-3" placeholder="Product price" id="price" onChange={onChange}/>
              </div>
              <div className="grid grid-cols-6 items-center text-right gap-3">
                <Label htmlFor="slug">Slug</Label>
                <Input className="col-span-3" placeholder="Product slug" id="slug" onChange={onChange}/>
              </div>
              <DialogFooter>
                <DialogClose asChild>
                  <Button type="button" variant="outline">Cancel</Button>
                </DialogClose>
                <Button type="submit">Create product</Button>
              </DialogFooter>
            </form>
          </DialogContent>
        </Dialog>
        
      </div>

      <div className="border rounded-lg p-2">
          <Table>
            <TableHeader>
              <TableHead>Id</TableHead>
              <TableHead>Product</TableHead>
              <TableHead>Slug</TableHead>
              <TableHead>Price</TableHead>
            </TableHeader>
            <TableBody>
            {
              products.map(product => (
                <TableRow key={product.id}>
                  <TableCell>{product.id}</TableCell>
                  <TableCell>{product.name}</TableCell>
                  <TableCell>{product.slug}</TableCell>
                  <TableCell>{product.price}</TableCell>
                </TableRow>
              ))
            }
            </TableBody>  
          </Table>
      </div>
    </div>
  )
}