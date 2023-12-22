package org.iesvdm.tienda.modelo;

import java.math.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;
import org.iesvdm.tienda.modelo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class TestExamenRecuperacionStream {

    @Test
    void testSkeletonCliente() {

        ClienteHome clienteHome = new ClienteHome();

        try {
            clienteHome.beginTransaction();

            List<Cliente> list = clienteHome.findAll();

            //TODO STREAMS
            list.stream().collect(toList());

            Assertions.assertEquals(36, list.stream().count());

            clienteHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            clienteHome.rollbackTransaction();
            throw e; // or display error message
        }
    }

    @Test
    void testSkeletonPedido() {

        PedidoHome pedidoHome = new PedidoHome();

        try {
            pedidoHome.beginTransaction();

            List<Pedido> list = pedidoHome.findAll();

            //TODO STREAMS
            list.stream().collect(toList());

            Assertions.assertEquals(115, list.stream().count());

            pedidoHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            pedidoHome.rollbackTransaction();
            throw e; // or display error message
        }
    }
    @Test
    void testSkeletonPago() {

        PagoHome pagoHome = new PagoHome();

        try {
            pagoHome.beginTransaction();

            List<Pago> list = pagoHome.findAll();

            //TODO STREAMS
            list.stream().collect(toList());

            Assertions.assertEquals(26, list.stream().count());

            pagoHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            pagoHome.rollbackTransaction();
            throw e; // or display error message
        }
    }
    /**
     * 1. Devuelve un listado con los distintos estados por los que puede pasar un pedido, sin repetición.
     */
    @Test
    void test1() {
        PedidoHome pedidoHome = new PedidoHome();

        try {
            pedidoHome.beginTransaction();

            List<Pedido> list = pedidoHome.findAll();

            //TODO STREAMS
            List<String> estados = list.stream()
                            .map(pedido -> pedido.getEstado())
                                    .distinct()
                                            .collect(toList());
            estados.forEach(System.out::println);


            pedidoHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            pedidoHome.rollbackTransaction();
            throw e; // or display error message
        }
    }


    /**
     * 2.Devuelve un listado con el código de cliente de aquellos clientes que realizaron algún pago en 2008.
     * Tenga en cuenta que deberá eliminar aquellos códigos de cliente que aparezcan repetidos.
     */
    @Test
    void test2() {
        ClienteHome clienteHome = new ClienteHome();

        try {
            clienteHome.beginTransaction();

            List<Cliente> list = clienteHome.findAll();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date ultimoDia2007 = sdf.parse("2017-12-31");
            Date primerDia2009 = sdf.parse("2009-01-01");

            //TODO STREAMS
            List<Pedido> listaDePedidos = (List<Pedido>) list.stream()
                    .flatMap(cliente -> cliente.getPedidos().stream())
                    .collect(toList());
            List<String> listaDeID = (List<String>) listaDePedidos.stream()
                            .filter(pedido -> pedido.getFechaPedido().after(ultimoDia2007) && pedido.getFechaPedido().before(primerDia2009))
                                    .map(pedido -> "ID" + pedido.getCliente().getCodigoCliente())
                                            .distinct().collect(toList());
            listaDeID.forEach(System.out::println);

            clienteHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            clienteHome.rollbackTransaction();
            throw e; // or display error message
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 3. Devuelve un listado con el código de pedido, código de cliente, fecha esperada
     * y fecha de entrega de los pedidos que no han sido entregados a tiempo.
     * NOTAS:
     * Revisa los campos de la tabla pedido haciendo doble click en el panel de Database.
     * Razona sobre los campos de la tabla.
     * Puede que tengas que tener cuidado con los NULL de la tabla o puede que no.
     */
    @Test
    void test3() {
        PedidoHome pedidoHome = new PedidoHome();

        try {
            pedidoHome.beginTransaction();

            List<Pedido> list = pedidoHome.findAll();

            //TODO STREAMS
            List<String> fechaPedidosTardios = list.stream()
                            .filter(pedido -> (pedido.getFechaEntrega() != null) && pedido.getFechaEntrega().after(pedido.getFechaEsperada()))
                                    .map(pedido -> "Codigo: " + pedido.getCodigoPedido() + " ID cliente: " + pedido.getCliente().getCodigoCliente() + " Fecha esperada: " + pedido.getFechaEsperada() + " Fecha de entrega: " + pedido.getFechaEntrega())
                                            .collect(toList());
            fechaPedidosTardios.forEach(System.out::println);

            pedidoHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            pedidoHome.rollbackTransaction();
            throw e; // or display error message
        }
    }

    /**
     * 4. Devuelve un listado con el código de pedido, código de cliente, fecha esperada
     * y fecha de entrega de los pedidos cuya fecha de entrega
     * ha sido al menos dos días antes de la fecha esperada.
     * NOTAS: fecha de entrega null significa que no se ha entregado.
     */
    @Test
    void test4() {

        //Prueba de aritmética de fechas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Utilizando la fecha de hoy
        c.add(Calendar.DATE, 2); // Sumando 2 días
        String output = sdf.format(c.getTime());
        System.out.println(output);
        //

        PedidoHome pedidoHome = new PedidoHome();

        try {
            pedidoHome.beginTransaction();

            List<Pedido> list = pedidoHome.findAll();

            //TODO STREAMS
            list.stream().filter(pedido -> {
            //Completa con la prueba de aritmética de fechas previa el filtro y el stream.
                return false;
            });

            pedidoHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            pedidoHome.rollbackTransaction();
            throw e; // or display error message
        }
    }

    /**
     * 5. Devuelve un listado con todos los pagos que se realizaron en el año 2008 mediante Paypal.
     * Ordene el resultado de mayor a menor según importe de pago.
     */
    @Test
    void test5() {
        PagoHome pagoHome = new PagoHome();

        try {
            pagoHome.beginTransaction();

            List<Pago> list = pagoHome.findAll();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date ultimoDia2007 = sdf.parse("2017-12-31");
            Date primerDia2009 = sdf.parse("2009-01-01");

            //TODO STREAMS
            List<String> pagos = list.stream()
                            .filter(pago -> (pago.getFechaPago().after(ultimoDia2007) && pago.getFechaPago().before(primerDia2009)) && pago.getFormaPago().equalsIgnoreCase("paypal"))
                                    .map(pago -> "Id pago: " + pago.getId() + " Fecha: " + pago.getFechaPago() + " Metodo de pago: " + pago.getFormaPago() )
                                            .collect(toList());
            pagos.forEach(System.out::println);


            pagoHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            pagoHome.rollbackTransaction();
            throw e; // or display error message
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 6. Muestra el nombre de las regiones de los clientes que hayan realizado pagos.
     */
    @Test
    void test6() {
        ClienteHome clienteHome = new ClienteHome();

        try {
            clienteHome.beginTransaction();

            List<Cliente> list = clienteHome.findAll();

            //TODO STREAMS
            List<String> regiones = list.stream()
                            .filter(cliente -> !cliente.getPagos().isEmpty())
                                    .map(cliente -> cliente.getRegion()).distinct().collect(toList());

            regiones.forEach(System.out::println);

            clienteHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            clienteHome.rollbackTransaction();
            throw e; // or display error message
        }
    }


    /**
     * 7. ¿Cuántos clientes existen con domicilio en la ciudad de Madrid?
     */
    @Test
    void test7() {
        ClienteHome clienteHome = new ClienteHome();

        try {
            clienteHome.beginTransaction();

            List<Cliente> list = clienteHome.findAll();

            //TODO STREAMS

            long numeroMadrid = list.stream()
                            .filter(cliente -> cliente.getCiudad().equalsIgnoreCase("madrid"))
                                    .count();
            System.out.println(numeroMadrid);


            clienteHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            clienteHome.rollbackTransaction();
            throw e; // or display error message
        }
    }

    /**
     * 8. Devuelve el nombre del cliente con mayor límite de crédito.
     */
    @Test
    void test8() {
        ClienteHome clienteHome = new ClienteHome();

        try {
            clienteHome.beginTransaction();

            List<Cliente> list = clienteHome.findAll();

            //TODO STREAMS
            List<String> clienteMayorLimite = list.stream()
                            .sorted(Comparator.comparing((Cliente cliente) -> cliente.getLimiteCredito()).reversed())
                                    .limit(1)
                                            .map(cliente -> cliente.getNombreCliente())
                                                    .collect(toList());
            clienteMayorLimite.forEach(System.out::println);


            clienteHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            clienteHome.rollbackTransaction();
            throw e; // or display error message
        }
    }

    /**
     *  9. Devuelve un listado con el número de clientes que tiene cada país.
     */
    @Test
    void test9() {
        ClienteHome clienteHome = new ClienteHome();

        try {
            clienteHome.beginTransaction();

            List<Cliente> list = clienteHome.findAll();

            //TODO STREAMS
            List<String> listadoPais = list.stream()
                            .map(cliente -> cliente.getPais())
                                    .distinct()
                                            .collect(toList());



            clienteHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            clienteHome.rollbackTransaction();
            throw e; // or display error message
        }
    }

    /**
     * 10. Calcula la fecha del primer y último pago realizado por cada uno de los clientes.
     * El listado deberá mostrar el nombre y los apellidos de cada cliente.
     */
    void test10() {
        ClienteHome clienteHome = new ClienteHome();

        try {
            clienteHome.beginTransaction();

            List<Cliente> list = clienteHome.findAll();

            //TODO STREAMS
            List<Pago> pagos = list.forEach(Cliente::getPago)
                            .

            clienteHome.commitTransaction();
        } catch (RuntimeException e) {
            e.printStackTrace();
            clienteHome.rollbackTransaction();
            throw e; // or display error message
        }
    }

}
