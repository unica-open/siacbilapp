<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp"/>
					<s:form id="formRipartizioneMutuo" cssClass="form-horizontal ignore-enter-key">
						<s:hidden name="mutuo.uid" />
						<s:hidden name="mutuo.numero" />
	                 		
	                    <h3>Ripartizione capitoli (rimborso prestito)</h3>
	
	                    <div class="step-content">
	                     <div class="step-pane active" id="step1">
	
	                       <fieldset class="form-horizontal">
	                           <h4>Dati mutuo</h4>
	                           <div class="control-group">
	                              <label class="control-label">Numero Mutuo</label>
	                              <div class="controls">
	                                 <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.numero" id="numeroMutuo" />
	                                 <span class="al"> <label class="radio inline" for="tipoTasso">Stato</label>
	                                 </span> <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.statoMutuo" id="statoMutuo"/>
	                              </div>
	                           </div>
	                           
	                           <div class="control-group">
	                           		<label class="control-label">Tipo Tasso</label>
	                           		<div class="controls">
	                                 </span> <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.tipoTasso" id="tipoTasso"/>
	
	                                 <span class="al"> <label class="radio inline" for="sommaMutuataIniziale">Importo</label></span>
	                                 <s:textfield disabled="true" id="sommaMutuataIniziale" name="mutuo.sommaMutuataIniziale" cssClass="span2 decimale soloNumeri forzaVirgolaDecimale"  />
	                                 
	                                 <span class="al"> <label class="radio inline" for="tassoInteresse">Tasso</label></span>
	                                 <s:textfield disabled="true" id="tassoInteresse" name="mutuo.tassoInteresse" cssClass="span2 decimale soloNumeri forzaVirgolaDecimale"  />
	                                 
	                           		</div>
	                           </div>
	
	                           <div class="control-group">
	                              <label class="control-label">Istituto di Credito</label>
	                              <div class="controls">
	                                 <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.soggetto.codiceSoggetto" id="codiceSoggetto" />
	                                 <s:textfield type="text" disabled="true" class="lbTextSmall span6" name="mutuo.soggetto.denominazione" />
	                              </div>
	                           </div>
	                           <div class="control-group">
	                              <label class="control-label">Durata</label>
	                              <div class="controls">
	                                 <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.durataAnni" id="durataAnni" />
	                                 
	                                 <span class="al"> <label class="radio inline" for="annoInizio">Dal</label></span>
	                                 <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.annoInizio" id="annoInizio" />
	                                 
	                                 <span class="al"> <label class="radio inline" for="annoFine">Al</label></span>
	                                 <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.annoFine" id="annoFine" />
	                                 
	                                 <span class="al"> <label class="radio inline" for="periodoRimborsoDescrizione">Periodo</label></span>
	                                 <s:textfield type="text" disabled="true" class="lbTextSmall span3" name="mutuo.periodoRimborso.descrizione" id="periodoRimborsoDescrizione"/>
	                              </div>
	                           </div>
	
	                           <h4>Ripartizione capitoli</h4>
	                           <s:if test="not mutuo.elencoRipartizioneMutuo.empty">
	                              <s:include value="/jsp/mutuo/include/elencoRipartizioneMutuo.jsp" />
	                           </s:if><s:else>
	                              Non &egrave; presente alcuna ripartizione
	                           </s:else>
	
		                      <div class="control-group">
                                 <label class="control-label" for="tipoRipartizione">Tipo ripartizione</label>
                                 <div class="controls">
                                    <s:select list="listaTipoRipartizioneMutuo" listKey="codice" listValue="descrizione" name="tipoRipartizioneMutuoStr" 
                                    id="tipoRipartizione" cssClass="span2" headerKey="" headerValue="" />
                                    <label class="radio inline" for="annoCapitolo">Anno</label>
                                    <s:textfield id="annoCapitolo" cssClass="lbTextSmall span1 numeroNaturale" name="ripartizioneMutuo.capitolo.annoCapitolo" value="%{bilancio.anno}" readonly="true"/>
                                    
                                    <label class="radio inline" for="numeroCapitolo">Capitolo</label>
                                    <s:textfield id="numeroCapitolo" cssClass="lbTextSmall span1 numeroNaturale" name="ripartizioneMutuo.capitolo.numeroCapitolo" />
                                    
                                    
                                    <label class="radio inline" for="numeroArticolo">Articolo</label>
                                    <s:textfield id="numeroArticolo" cssClass="lbTextSmall span1 numeroNaturale" name="ripartizioneMutuo.capitolo.numeroArticolo" />
                                    
                                    <span class="radio guidata">
                                       <button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataCapitolo">compilazione guidata</button>
                                    </span>
                                    
                                    
                                    <label class="radio inline" for="importo">Importo</label>
                                    <s:textfield id="importo" cssClass="lbTextSmall span1 decimale forzaVirgolaDecimale" name="ripartizioneMutuo.ripartizioneImporto" />
                                    <button type="button" class="btn aggiungiRipartizione">Aggiungi</button>
                                 </div>
                              </div>
	                  		<br/>
	
	                     	</fieldset>
	                     </div>
	                  </div>
	                  <div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
	                     <div class="modal-body">
	                        <div class="alert alert-error">
	                           <button type="button" class="close" data-hide="alert">&times;</button>
	                           <p>
	                              <strong>Attenzione!</strong>
	                           </p>
	                           <p>Stai per annullare la ripartizione: sei sicuro di voler proseguire?</p>
	                        </div>
	                     </div>
	                     <div class="modal-footer">
	                        <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	                        <button class="btn btn-primary" formmethod="post" type="submit" formaction="ripartizioneMutuo_annulla.do">si, prosegui</button>
	                     </div>
	                  </div>                  
	                  <br/>
	 				  <p class="margin-medium">
					  	<s:include value="/jsp/include/indietro.jsp" />
						<s:submit value="annulla" cssClass="btn" action="ripartizioneMutuo" />
	                   	<s:if test='not mutuo.elencoRipartizioneMutuo.empty'>
							<span> <a href='#msgAnnulla' data-toggle='modal' class="btn">annulla ripartizione</a></span>					
		                    <s:submit value="esporta ripartizione capitoli in excel" cssClass="btn scaricaRipartizioneExcel disableOverlay" action="ripartizioneMutuo_scaricaRipartizioneExcel" />
	                 	</s:if>
                        <s:submit id="salva" value="salva" cssClass="btn btn-primary pull-right salva" action="ripartizioneMutuo_salva"/>
	                 	</p>  	
	                 	
	                 	<s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp" />
				</s:form>
			</div>	
		</div>	
	</div>	 
	</div>	

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/mutuo/ripartizioneMutuo.js"></script>
	
</body>
</html>