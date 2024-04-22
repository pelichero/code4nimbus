import { Button } from "./components/ui/button";
import { Input } from "./components/ui/input";
import { Search, PlusCircle } from "lucide-react"

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

  const [repositories, setRepositories] = useState<Repository[]>([])
  useEffect(() => {
    axios.get('http://localhost:9000/products')
      .then(response => setRepositories(response.data))
  } , [])

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
            <form className="space-y-6">
              <div className="grid grid-cols-6 items-center text-right gap-3">
                <Label htmlFor="name">Product</Label>
                <Input className="col-span-3" placeholder="Product name" id="name"/>
              </div>
              <div className="grid grid-cols-6 items-center text-right gap-3">
                <Label htmlFor="price">Price</Label>
                <Input className="col-span-3" placeholder="Product price" id="price"/>
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
            repositories.map(repository => (
              <div key={repository.id}>
                <h1>{repository.name}</h1>
                <p>{repository.slug}</p>
                <p>{repository.price}</p>
              </div>
            ))}


                {
                Array.from({ length: 10 }).map((_, i) => {
                  return (
                    <TableRow key={i}>
                      <TableCell>{i}</TableCell>
                      <TableCell>Product {i}</TableCell>
                      <TableCell>U$ 100,00</TableCell>
                      <TableCell>/product/{i}</TableCell>
                    </TableRow>
                  )
                })}
            </TableBody>  
          </Table>
      </div>
    </div>
  )
}