package mx.edu.utez.exception;

import org.ietf.jgss.GSSException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.w3c.dom.events.EventException;
import org.w3c.dom.ls.LSException;

import java.awt.color.ProfileDataException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.IllegalPathStateException;
import java.awt.print.PrinterException;
import java.beans.IntrospectionException;
import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.SyncFailedException;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.HttpRetryException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.nio.BufferOverflowException;
import java.nio.channels.InterruptedByTimeoutException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.*;
import java.nio.file.ProviderNotFoundException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.zip.DataFormatException;

import javax.activation.UnsupportedDataTypeException;
import javax.annotation.processing.FilerException;
import javax.imageio.IIOException;
import javax.lang.model.UnknownEntityException;
import javax.lang.model.type.MirroredTypesException;
import javax.management.BadStringOperationException;
import javax.management.JMRuntimeException;
import javax.print.PrintException;
import javax.print.attribute.UnmodifiableSetException;
import javax.script.ScriptException;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.RefreshFailedException;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.sound.sampled.LineUnavailableException;
import javax.sql.rowset.RowSetWarning;
import javax.sql.rowset.serial.SerialException;
import javax.sql.rowset.spi.SyncFactoryException;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.transaction.SystemException;
import javax.xml.bind.TypeConstraintException;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

@ControllerAdvice
public class ApiExceptionHandler {

	@ResponseBody
	@ExceptionHandler(value = { Throwable.class })
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public ApiError handleConflict(Exception ex) {
		return error(ex);
	}

	private ApiError error(Exception rawException) {
		Exception ex;
		if (rawException.getCause() != null) {
			ex = (Exception) rawException.getCause();
		} else {
			ex = rawException;
		}
		if (ex instanceof NullPointerException) {
			return new ApiError("Se intentó realizar una acción con datos faltantes",
					 "PIGETI00001");
		} else if (ex instanceof ArithmeticException) {
			return new ApiError("Se ha producido una condición aritmética excepcional",
					 "PIGETI00002");
		} else if (ex instanceof AnnotationTypeMismatchException) {
			return new ApiError(
					"Se  ha intentado acceder a un elemento de una anotación cuyo tipo ha cambiado después de que la anotación fue compilada",
					 "PIGETI00003");
		} else if (ex instanceof ClassCastException) {
			return new ApiError(
					"El código ha intentado convertir un objeto en una subclase de la que no es una instanciao",
					 "PIGETI00004");
		} else if (ex instanceof EnumConstantNotPresentException) {
			return new ApiError(
					"Una aplicación intentó acceder a una constante de enumeración por nombre y el tipo de enumeración no contiene ninguna constante con el nombre especificado",
					 "PIGETI00005");
		} else if (ex instanceof EventException) {
			return new ApiError("Se realizarón operaciones de eventos",
					 "PIGETI00006");
		} else if (ex instanceof FileSystemAlreadyExistsException) {
			return new ApiError(
					"Se lanzó una excepción de tiempo de ejecución, se intentó crear un sistema de archivos que ya existe",
					 "PIGETI00007");
		} else if (ex instanceof FileSystemNotFoundException) {
			return new ApiError(
					"Se lanzó una excepción de tiempo de ejecución, no se puedó encontrar un sistema de archivos",
					 "PIGETI00008");
		} else if (ex instanceof IllegalMonitorStateException) {
			return new ApiError(
					" un subproceso ha intentado esperar en el monitor de un objeto o para notificar a otros subprocesos que esperan en el monitor de un objeto",
					 "PIGETI00010");
		} else if (ex instanceof IllegalPathStateException) {
			return new ApiError(
					"Se realizó una operación en una ruta que está en un estado ilegal con respecto a la operación particular que se está realizando",
					 "PIGETI00011");
		} else if (ex instanceof IllegalStateException) {
			return new ApiError("Se ha invocado un método en un momento ilegal o inapropiado",
					 "PIGETI00012");
		} else if (ex instanceof IllformedLocaleException) {
			return new ApiError("Un argumento no es una etiqueta BCP 47 bien formada",
					 "PIGETI00013");
		} else if (ex instanceof IncompleteAnnotationException) {
			return new ApiError(
					"Un programa ha intentado acceder a un elemento de un tipo de anotación que se agregó a la definición del tipo de anotación después de que la anotación fue compilada",
					 "PIGETI00014");
		} else if (ex instanceof IndexOutOfBoundsException) {
			return new ApiError(
					"Un índice de algún tipo (como un arreglo, una cadena o un vector) está fuera de rango.",
					 "PIGETI00015");
		} else if (ex instanceof JMRuntimeException) {
			return new ApiError("Excepciones de tiempo de ejecución emitidas por implementaciones de JMX",
					 "PIGETI00016");
		} else if (ex instanceof LSException) {
			return new ApiError("Se generó una LSException porque se detuvo el procesamiento",
					 "PIGETI00017");
		} else if (ex instanceof MalformedParameterizedTypeException) {
			return new ApiError(
					"Un método reflectivo encontró un tipo parametrizado semánticamente mal formado que necesita instanciarlo",
					 "PIGETI00018");
		} else if (ex instanceof MirroredTypesException) {
			return new ApiError(
					"Una aplicación intentó acceder a una secuencia de objetos Class, cada uno de los cuales corresponde a un TypeMirro",
					 "PIGETI00019");
		} else if (ex instanceof MissingResourceException) {
			return new ApiError("Falta un recurso",
					"PIGETI00020");
		} else if (ex instanceof NoSuchElementException) {
			return new ApiError("No hay más elementos en la enumeración",
					 "PIGETI00021");
		} else if (ex instanceof ProfileDataException) {
			return new ApiError("Error al acceder o procesar un objeto ICC_Profile",
					 "PIGETI00022");
		} else if (ex instanceof ProviderException) {
			return new ApiError(
					"excepción de tiempo de ejecución para excepciones de proveedor (como errores de configuración incorrecta o errores internos irrecuperables)",
					 "PIGETI00023");
		} else if (ex instanceof ProviderNotFoundException) {
			return new ApiError(
					"Excepción de tiempo de ejecución, no se pudó encontrar un proveedor del tipo requerido.",
					 "PIGETI00024");
		} else if (ex instanceof SecurityException) {
			return new ApiError("Violación de seguridad",
					"PIGETI00025");
		} else if (ex instanceof SystemException) {
			return new ApiError("Excepciones estándar de CORBA",
					 "PIGETI00026");
		} else if (ex instanceof TypeConstraintException) {
			return new ApiError("Se detectó una infracción de una restricción de tipo comprobada dinámicamente",
					 "PIGETI00027");
		} else if (ex instanceof TypeNotPresentException) {
			return new ApiError(
					"Una aplicación intentó acceder a un tipo utilizando una cadena que representa el nombre del tipo, pero no se puede encontrar una definición para el tipo con el nombre especificad",
					 "PIGETI00028");
		} else if (ex instanceof UndeclaredThrowableException) {
			return new ApiError(
					"No se puede asignar a ninguno de los tipos de excepción declarados en la cláusula throws del método que se invocó en la instancia de proxy",
					 "PIGETI00029");
		} else if (ex instanceof UnknownEntityException) {
			return new ApiError("Se encontró un tipo de entidad desconocida",
					 "PIGETI00030");
		} else if (ex instanceof UnmodifiableSetException) {
			return new ApiError(
					"La operación solicitada no se puede realizar porque el conjunto no se puede modificar.",
					 "PIGETI00031");
		} else if (ex instanceof UnsupportedOperationException) {
			return new ApiError("La operación solicitada no es compatible",
					 "PIGETI00032");
		} else if (ex instanceof WrongMethodTypeException) {
			return new ApiError(
					"El código ha intentado llamar a un identificador de método a través del tipo de método incorrecto",
					 "PIGETI00033");
		} else if (ex instanceof AlreadyBoundException) {
			return new ApiError(
					"Se intentó vincular un objeto en el registro a un nombre que ya tiene una vinculación asociada",
					 "PIGETI00036");
		} else if (ex instanceof BadLocationException) {
			return new ApiError("Existen ubicaciones incorrectas dentro de un modelo de documento",
					 "PIGETI00038");
		} else if (ex instanceof BadStringOperationException) {
			return new ApiError("Una operación de cadena no válida a un método para construir una consulta",
					 "PIGETI00039");
		} else if (ex instanceof CertificateException) {
			return new ApiError("Existe una variedad de problemas de certificados",
					 "PIGETI00040");
		} else if (ex instanceof CloneNotSupportedException) {
			return new ApiError(
					"Se ha llamado al método clon en la clase Object para clonar un objeto, pero que la clase del objeto no implementa la interfaz Cloneable",
					 "PIGETI00041");
		} else if (ex instanceof DataFormatException) {
			return new ApiError("Se ha producido un error de formato de datos",
					 "PIGETI00042");
		} else if (ex instanceof DatatypeConfigurationException) {
			return new ApiError("Error de configuración grave",
					"PIGETI00043");
		} else if (ex instanceof DestroyFailedException) {
			return new ApiError("Falló una operación de destrucción",
					 "PIGETI00044");
		} else if (ex instanceof ExecutionException) {
			return new ApiError(
					"Se intentó recuperar el resultado de una tarea que se canceló al lanzar una excepción",
					 "PIGETI00045");
		} else if (ex instanceof GeneralSecurityException) {
			return new ApiError(
					"Excepción de seguridad genérica que proporciona seguridad de tipos para todas las clases de excepción relacionadas con la seguridad",
					 "PIGETI00046");
		} else if (ex instanceof GSSException) {
			return new ApiError("Se produjo error de GSS-API, incluido cualquier error específico del mecanismo",
					 "PIGETI00049");
		} else if (ex instanceof IllegalClassFormatException) {
			return new ApiError(
					"Error de implementación de ClassFileTransformer.transform cuando los parámetros de entrada no son válidos",
					 "PIGETI00050");
		} else if (ex instanceof IntrospectionException) {
			return new ApiError("Ocurrió una excepción durante la introspección",
					 "PIGETI00051");
		} else if (ex instanceof CharacterCodingException) {
			return new ApiError("Se produjó un error de codificación o decodificación de caracteres",
					 "PIGETI00053");
		} else if (ex instanceof FileNotFoundException) {
			return new ApiError(
					"Ha fallado un intento de abrir el archivo indicado por un nombre de ruta especificado",
					 "PIGETI00054");
		} else if (ex instanceof FilerException) {
			return new ApiError(
					"Se detectó un intento de abrir un archivo que violaría las garantías proporcionadas por el Filter",
					 "PIGETI00055");
		} else if (ex instanceof FileSystemException) {
			return new ApiError("Falló una operación del sistema de archivos en uno o dos archivos",
					 "PIGETI00056");
		} else if (ex instanceof HttpRetryException) {
			return new ApiError(
					"Se debe reintentar una solicitud HTTP, pero no se puede reintentar automáticamente",
					 "PIGETI00057");
		} else if (ex instanceof IIOException) {
			return new ApiError("Existen fallas en el tiempo de ejecución de operaciones de lectura y escritura",
					 "PIGETI00058");
		} else if (ex instanceof InterruptedByTimeoutException) {
			return new ApiError(
					"Excepción comprobada recibida por un subproceso cuando transcurre un tiempo de espera antes de que se complete una operación asincrónica",
					 "PIGETI00059");
		} else if (ex instanceof MalformedURLException) {
			return new ApiError(
					"Se ha producido una URL con formato incorrecto. No se pudo encontrar ningún protocolo legal",
					 "PIGETI00060");
		} else if (ex instanceof ProtocolException) {
			return new ApiError("Existe un error en el protocolo subyacente, como un error de TCP",
					 "PIGETI00061");
		} else if (ex instanceof RemoteException) {
			return new ApiError(
					"Excepciones relacionadas con la comunicación que pueden ocurrir durante la ejecución de una llamada a un método remoto",
					 "PIGETI00062");
		} else if (ex instanceof SyncFailedException) {
			return new ApiError("Ha fallado una operación de sincronización",
					 "PIGETI00063");
		} else if (ex instanceof UnknownHostException) {
			return new ApiError("No se pudo determinar la dirección IP de un host",
					 "PIGETI00064");
		} else if (ex instanceof UnknownServiceException) {
			return new ApiError("Se ha producido una excepción de servicio desconocida",
					 "PIGETI00065");
		} else if (ex instanceof UnsupportedDataTypeException) {
			return new ApiError("La operación solicitada no admite el tipo de datos solicitado",
					 "PIGETI00066");
		} else if (ex instanceof UnsupportedEncodingException) {
			return new ApiError("No se admite la codificación de caracteres",
					 "PIGETI00067");
		} else if (ex instanceof UTFDataFormatException) {
			return new ApiError(
					"Una cadena con formato incorrecto en formato UTF-8 modificado ha sido leída en un flujo de entrada de datos",
					 "PIGETI00068");
		} else if (ex instanceof LineUnavailableException) {
			return new ApiError("Una línea no se puede abrir porque no está disponible",
					 "PIGETI00069");
		} else if (ex instanceof ParseException) {
			return new ApiError("Se ha producido un error inesperadamente durante el parseo",
					 "PIGETI00071");
		} else if (ex instanceof ParserConfigurationException) {
			return new ApiError("Error de configuración grave",
					"PIGETI00072");
		} else if (ex instanceof PrinterException) {
			return new ApiError("Se ha producido una condición excepcional en el sistema de impresión",
					 "PIGETI00073");
		} else if (ex instanceof PrintException) {
			return new ApiError(
					"Error relacionada con la impresión que se produjo durante el uso de una instancia del servicio de impresión",
					 "PIGETI00074");
		} else if (ex instanceof PrivilegedActionException) {
			return new ApiError("La acción que se está realizando arrojó una excepción marcada",
					 "PIGETI00075");
		} else if (ex instanceof PropertyVetoException) {
			return new ApiError("Un cambio propuesto en una propiedad representa un valor inaceptable.",
					 "PIGETI00076");
		} else if (ex instanceof ReflectiveOperationException) {
			return new ApiError("Excepciones lanzadas por operaciones de reflexión en la reflexión del núcleo",
					 "PIGETI00077");
		} else if (ex instanceof RefreshFailedException) {
			return new ApiError("Falló una operación de actualización",
					 "PIGETI00084");
		} else if (ex instanceof ScriptException) {
			return new ApiError("Excepción genérica para las API de secuencias de comandos",
					 "PIGETI00086");
		} else if (ex instanceof ServerNotActiveException) {
			return new ApiError("Una excepción lanzada durante una llamada a RemoteServer.getClientHost",
					 "PIGETI00087");
		} else if (ex instanceof BatchUpdateException) {
			return new ApiError("Se produjó un error durante una operación de actualización por lotes",
					 "PIGETI00089");
		} else if (ex instanceof RowSetWarning) {
			return new ApiError("Advertencias de la base de datos establecidas en los objetos RowSet",
					 "PIGETI00090");
		} else if (ex instanceof SerialException) {
			return new ApiError("Error con la serialización o deserialización de tipos SQL",
					 "PIGETI00091");
		} else if (ex instanceof SQLClientInfoException) {
			return new ApiError(
					"Una o más propiedades de información del cliente no se pueden establecer en una conexión",
					 "PIGETI00092");
		} else if (ex instanceof SQLNonTransientException) {
			return new ApiError("Una instancia en la que un reintento de la misma operación falló",
					 "PIGETI00093");
		} else if (ex instanceof SQLTransientException) {
			return new ApiError("Una operación fallida anteriormente podría tener éxito si se reintenta",
					 "PIGETI00098");
		} else if (ex instanceof BadCredentialsException) {
			return new ApiError("Se rechazó una solicitud de autenticación porque las credenciales no son válidas",
					 "PIGETI00121");
		}  else if (ex instanceof DisabledException) {
			return new ApiError("Se rechazó una solicitud de autenticación porque la cuenta está deshabilitada",
					 "PIGETI00122");
		}  else if (ex instanceof LockedException) {
			return new ApiError("Se rechazó una solicitud de autenticación porque la cuenta está bloqueada",
					 "PIGETI00123");
		}  else if (ex instanceof AuthenticationCredentialsNotFoundException) {
			return new ApiError("Se rechazó una solicitud de autenticación porque no hay ningún objeto de autenticación",
					 "PIGETI00126");
		} else if (ex instanceof NonceExpiredException) {
			return new ApiError("Se rechazó una solicitud de autenticación porque el código de resumen ha caducado",
					 "PIGETI00127");
		}else if (ex instanceof SessionAuthenticationException) {
			return new ApiError("Un objeto de autenticación no es válido para la sesión actual",
					 "PIGETI00129");
		} else if (ex instanceof UsernameNotFoundException) {
			return new ApiError("No puedó localizar a un usuario por su nombre de usuario",
					 "PIGETI00130");
		} else if (ex instanceof AccountStatusException) {
			return new ApiError("Error en un estado de cuenta de un usuario particular",
					 "PIGETI00131");
		} else if (ex instanceof BufferOverflowException) {
			return new ApiError("Se alcanzó el límite del búfer de destino",
					 "PIGETI00132");
		}else if (ex instanceof TimeoutException) {
			return new ApiError("Se agotó el tiempo de espera de una operación de bloqueo",
					 "PIGETI00106");
		}else if (ex instanceof SQLRecoverableException) {
			return new ApiError(
					"Una operación fallida anteriormente podría tener éxito si se realiza algunos pasos de recuperación",
					 "PIGETI00102");
		} else if (ex instanceof SQLWarning) {
			return new ApiError("Se proporciona información sobre las advertencias de acceso a la base de datos",
					 "PIGETI00103");
		} else if (ex instanceof SyncFactoryException) {
			return new ApiError(
					"Una implementación de RowSet desconectada no se puede usar sin que se haya instanciado correctamente un SyncProvider",
					 "PIGETI00104");
		} else if (ex instanceof TooManyListenersException) {
			return new ApiError(
					"Modelo de eventos para anotar e implementar un caso especial de unidifusión",
					 "PIGETI00107");
		} else if (ex instanceof UnexpectedRollbackException) {
			return new ApiError("Este registro ya se encuentra",
					 "PIGETI00133");
		}else if (ex instanceof HttpRequestMethodNotSupportedException) {
			return new ApiError("El controlador de solicitudes no admite el método de solicitud específicado",
					 "PIGETI00134");
		}else if (ex instanceof MethodArgumentNotValidException) {
			return new ApiError("Fallo la validación de un argumento",
					 "PIGETI00135");
		}else if (ex instanceof DataIntegrityViolationException) {
            return new ApiError("Se intento de insertar o actualizar datos y hubo una violación de una restricción de integridad",
                     "PIGETI00136");
        }else if (ex instanceof DataRetrievalFailureException) {
            return new ApiError("No se pudieron recuperar ciertos datos esperados",
                     "PIGETI00137");
        }else if (ex instanceof ConcurrentModificationException) {
            return new ApiError("Se han detectado modificaciones concurrentes de un objeto cuando dicha modificación no está permitida.",
                     "PIGETI00139");
        }else if (ex instanceof EmptyStackException) {
            return new ApiError("la pila está vacía",
                     "PIGETI00140");
        }else if (ex instanceof UnsupportedAddressTypeException) {
            return new ApiError("Se intentó enlazar o conectar a una dirección de socket de un tipo que no es compatible.\n",
                     "PIGETI00141");
        }else if (ex instanceof UnsupportedCharsetException) {
            return new ApiError("No hay soporte disponible para un juego de caracteres solicitado",
                     "PIGETI00142");
        }else if (ex instanceof ProviderMismatchException) {
            return new ApiError("Se intentó invocar un método en un objeto creado por un proveedor de sistema",
                     "PIGETI00143");
        }else if (ex instanceof InvalidParameterException) {
            return new ApiError("Se pasó un parámetro no válido a un método",
                     "PIGETI00145");
        }else if (ex instanceof InvalidPathException) {
            return new ApiError("La cadena de ruta no se puede convertir en una ruta porque la cadena de ruta contiene caracteres no válidos",
                     "PIGETI00146");
        }else if (ex instanceof TransformerException) {
			return new ApiError("Una condición excepcional ocurrió durante el proceso de transformación",
					 "PIGETI00118");
		}else if (ex instanceof TransformException) {
			return new ApiError(
					"Una condición excepcional que se produjó al ejecutar un algoritmo de transformación",
					 "PIGETI00109");
		} else if (ex instanceof UnmodifiableClassException) {
			return new ApiError(
					"Una implementación de Instrumentation.redefineClasses de las clases especificadas no se puede modificar",
					 "PIGETI00110");
		} else if (ex instanceof UnsupportedCallbackException) {
			return new ApiError("No reconoce una devolución de llamada en particular",
					 "PIGETI00111");
		} else if (ex instanceof UnsupportedFlavorException) {
			return new ApiError("Los datos solicitados no son compatibles con este tipo",
					 "PIGETI00112");
		} else if (ex instanceof UnsupportedLookAndFeelException) {
			return new ApiError(
					"Las clases de gestión de apariencia solicitadas no están presentes en el sistema del usuario",
					 "PIGETI00113");
		} else if (ex instanceof URIReferenceException) {
			return new ApiError("Una condición excepcional lanzada al desreferenciar una URIReference",
					 "PIGETI00114");
		} else if (ex instanceof URISyntaxException) {
			return new ApiError("Una cadena no se pudo analizar como una referencia de URI.",
					 "PIGETI00115");
		} else if (ex instanceof NumberFormatException) {
			return new ApiError("Ha intentado convertir una cadena a uno de tipo numérico",
					 "PIGETI00116");
		} else if (ex instanceof RuntimeException) {
			return new ApiError(
					"Ocurrió algo que no permitió realizar la operación",
					 "PIGETI00085");
		} else if (ex instanceof IOException) {
			return new ApiError("Se ha producido una excepción de I/O de algún tipo",
					 "PIGETI00052");
		} else if (ex instanceof SQLException) {
			return new ApiError("Un error de acceso a la base de datos u otros errores",
					 "PIGETI00088");
		} else {
			return new ApiError(ex.getMessage(),
					"PIGETI00000");
		}
	}
}
