package mx.edu.utez.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import mx.edu.utez.dto.NumeroTarjetaDto;
import mx.edu.utez.exception.ApiRequestException;
import mx.edu.utez.service.NumeroTarjetaService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import mx.edu.utez.model.NumeroTarjeta;


@RestController
@RequestMapping("/app/tarjeta/")
public class NumeroTarjetaController {

	@Autowired
	private NumeroTarjetaService numeroTarjetaService;
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping(value="listar")
	public List<NumeroTarjeta> obtenerTarjetas(HttpServletRequest httpServletRequest) throws ApiRequestException {
		return  numeroTarjetaService.obtenerTarjetasCliente(httpServletRequest);
	}

	@GetMapping(value="{id}")
	public NumeroTarjeta obtener(@PathVariable long id) throws ApiRequestException {
		return  numeroTarjetaService.obtenerTarjeta(id);
	}

	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping(value="registrar")
	public boolean guardar(@Valid @RequestBody NumeroTarjetaDto tarjeta,  HttpServletRequest httpServletRequest) throws ApiRequestException {
		return numeroTarjetaService.registrar(convertToEntity(tarjeta),httpServletRequest);
	}
	@PreAuthorize("hasRole('CLIENTE')")
	@DeleteMapping(value="eliminar/{id}")
	public boolean eliminar(@PathVariable long id, HttpServletRequest httpServletRequest) throws ApiRequestException {

		return numeroTarjetaService.borrar(id,httpServletRequest);
	}

	@PreAuthorize("hasRole('CLIENTE')")
	@PutMapping (value="actualizar")
	public boolean actualizar(@Valid @RequestBody NumeroTarjetaDto tarjeta, HttpServletRequest httpServletRequest) throws ApiRequestException {
		return numeroTarjetaService.actualizar(convertToEntity(tarjeta), httpServletRequest);
	}

	@PreAuthorize("hasRole('CLIENTE')")
	@DeleteMapping(value = "bajalogica/{id}")
	public boolean bajaLogica(@PathVariable long id, HttpServletRequest httpServletRequest) throws ApiRequestException {

		return numeroTarjetaService.bajaLogica(id, httpServletRequest);
	}
	
	private NumeroTarjeta convertToEntity(NumeroTarjetaDto numeroTarjetaDto){
		return modelMapper.map(numeroTarjetaDto, NumeroTarjeta.class);
	}

}
