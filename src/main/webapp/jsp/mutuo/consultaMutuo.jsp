<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
 <%@ taglib uri="/struts-tags" prefix="s"%> 
 <%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>	

	<s:include value="/jsp/include/header.jsp" />
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
					<s:include value="/jsp/include/messaggi.jsp" />
					
               <s:form id="formConsultaMutuo" cssClass="form-horizontal" >
                  <s:hidden name="mutuo.uid" />
				<h3>Consulta Mutuo</h3>
				
				<h4 class="step-pane">Mutuo: <s:property value="mutuo.numero" /></h4>
				
					<ul class="nav nav-tabs">
						<li class="active"><a href="#mutuo" data-toggle="tab">Mutuo</a></li>
						<li ><a href="#ammortamento" data-toggle="tab">Ammortamento</a></li>
						<li><a href="#movimenti-contabili" data-toggle="tab">Movimenti Contabili</a></li>
						<li><a href="#progetti" data-toggle="tab">Progetti</a></li>
						<li><a href="#ripartizione-capitoli" data-toggle="tab">Ripartizione Capitoli</a></li>	
					</ul>

					<div class="tab-content">
						<div class="tab-pane active" id="mutuo">
							<dl class="dl-horizontal">
                              <div class="boxOrSpan2" >
	                              <div class="boxOrInLeft">
	                              <!-- inizio dati mutuo -->
	                              <p>Mutuo</p>
		                              <ul  class="htmlelt">
		                              
		                              <li>
										<dfn>Stato Mutuo</dfn>
										<dl><s:property value="mutuo.statoMutuo"/></dl>
									  </li>
									  
									  <li>
										<dfn>Tipo Tasso</dfn>
										<dl><s:property value="mutuo.tipoTasso"/></dl>
									  </li>	
		
		                              <li> 
										<dfn>Data Atto Mutuo</dfn>
										<dl><s:property value="mutuo.dataAtto"/></dl>
		                              </li>
		
		                              <li>
										<dfn>Oggetto del mutuo</dfn>
										<dl><s:property value="mutuo.oggetto"/>&nbsp;</dl>
		                              </li> 
									
									</ul>
									<!-- fine dati mutuo -->
									
									 <!-- inizio piano ammortamento -->
	                              <p>Piano di Ammortamento</p>
		                              <ul  class="htmlelt">
		                              
		                              <li>
										<dfn>Periodo di ammortamento</dfn>
										<dl>dal <s:property value="mutuo.annoInizio"/> al <s:property value="mutuo.annoFine"/> </dl>
									  </li>
									  
									  <li>
										<dfn>Periodo scadenza</dfn>
										<dl><s:property value="mutuo.periodoRimborso.descrizione"/></dl>
									  </li>	
		
		                              <li> 	
										<dfn>Scadenza prima rata</dfn>
										<dl><s:property value="mutuo.scadenzaPrimaRata"/></dl>
		                              </li>
		
		                              <li>
										<dfn>Importo</dfn>
										<dl><s:property value="mutuo.sommaMutuataIniziale"/>&nbsp;</dl>
		                              </li> 
		                              
		                              <li>
										<dfn>Tasso di interesse</dfn>
										<dl><s:property value="mutuo.tassoInteresse"/>&nbsp;</dl>
		                              </li> 
										
									 <s:if test="mutuo.tipoTasso.variabile">	
										  <li>
											<dfn>Euribor</dfn>
											<dl><s:property value="mutuo.tassoInteresseEuribor"/>&nbsp;</dl>
			                              </li> 
			                              
			                              <li>
											<dfn>Spread</dfn>
											<dl><s:property value="mutuo.tassoInteresseSpread"/>&nbsp;</dl>
			                              </li> 
		                              </s:if>
		                              
		                              <li>
										<dfn>Debito residuo</dfn>
										<dl><s:property value="mutuo.debitoResiduo"/>&nbsp;</dl>
		                              </li> 
		                              
		                              <li>
										<dfn>Annualit&agrave; <i class="icon-info-sign" 
                                            data-toggle="popover" 
                                            data-original-title="" 
                                            data-trigger="hover" 
                                            data-content="Quote capitale e interessi delle rate nell'anno (esclusi gli oneri)"></i></dfn>
										<dl><s:property value="mutuo.annualita"/>&nbsp;</dl>
		                              </li> 
		                              
		                               <li>
										<dfn>Preammortamento</dfn>
										<dl><s:property value="mutuo.preammortamento"/>&nbsp;</dl>
		                              </li> 
									
									</ul>
									<!-- fine piano ammortamento -->
									</div>
									
									
										<div class="boxOrInRight">
			                               <!-- inizio soggetto -->
			                              <p>Soggetto</p>
										  <ul class="htmlelt">
				                              <li> 
												<dfn>Istituto di credito</dfn>
												<dl><s:property value="mutuo.soggetto.codiceDenominazione" />&nbsp;</dl>
											  </li>
							                  <li> 
												<dfn>Tipo finanziamento</dfn>
												<dl><s:property value="mutuo.tipoFinanziamento.descrizione" />&nbsp;</dl>
											  </li>
							                  <li> 
												<dfn>Sottoconto bancario</dfn>
												<dl><s:property value="mutuo.contoTesoreria.codiceDescrizione" />&nbsp;</dl>
											  </li>	
			                              </ul>
			                              <!-- fine soggetto -->
			                            <!-- inizio provvedimento -->
			                              <p>Provvedimento</p>
										  <ul class="htmlelt">
			                              
				                              <li> 
												<dfn>Tipo</dfn>
												<dl><s:property value="mutuo.attoAmministrativo.tipoAtto.codice" />&nbsp;</dl>
											  </li>
				                              
				                              <li> 
												<dfn>Anno</dfn>
												<dl><s:property value="mutuo.attoAmministrativo.anno" />&nbsp;</dl>
											  </li>
											  
											  <li> 
												<dfn>Numero</dfn>
												<dl><s:property value="mutuo.attoAmministrativo.numero" />&nbsp;</dl>
											  </li>
											  
											  <li> 
												<dfn>Oggetto</dfn>
												<dl><s:property value="mutuo.attoAmministrativo.oggetto" />&nbsp;</dl>
											  </li>
										  
										   	  <li> 
												<dfn>Struttura</dfn>
												<dl><s:property value="mutuo.attoAmministrativo.strutturaAmmContabile.codice" />&nbsp; - <s:property value="mutuo.attoAmministrativo.strutturaAmmContabile.descrizione" />&nbsp;</dl>
											  </li>
			                              
			                              </ul>
			                            <!-- fine provvedimento -->
			                            </div>
								</div>	
								</dl>
							</div>
							
						<div class="tab-pane active" id="ammortamento">
							<dl class="dl-horizontal">
	                              <div class="boxOrInCenter">
	                              <h4>Rate di Mutuo</h4>
			                           <s:if test="not mutuo.elencoRate.empty">
			                              <s:include value="/jsp/mutuo/include/elencoRateMutuoConsulta.jsp" />
			                              <p>
                   							 <s:submit value="esporta piano ammortamento in excel" cssClass="btn disableOverlay" action="consultaMutuo_scaricaPianoAmmortamentoExcel" />
			                            	
			                              </p>
			                           </s:if><s:else>
			                              Non sono presenti rate.
			                           </s:else>
		                       	 </div>
		                     </dl>
						</div>
							
						<div class="tab-pane active" id="movimenti-contabili">
							<dl class="dl-horizontal">
	                              <div class="boxOrInCenter">
	                               <h4>Impegni</h4>
                           		   <s:if test="not mutuo.elencoImpegniAssociati.empty">
			                       		<s:include value="/jsp/mutuo/include/elencoImpegniAssociatiMutuoConsulta.jsp" />
                           		   </s:if><s:else>
                              			Non sono presenti impegni associati.
                           		   </s:else>
                           
                           		   <h4>Accertamenti</h4>
                           		   <s:if test="not mutuo.elencoAccertamentiAssociati.empty">
			                              <s:include value="/jsp/mutuo/include/elencoAccertamentiAssociatiMutuoConsulta.jsp" />
                           		   </s:if><s:else>
                              			Non sono presenti accertamenti associati.
                           		   </s:else>
                           		   <p>
			                         	<s:submit value="esporta movimenti associati in excel" cssClass="btn disableOverlay" action="consultaMutuo_scaricaMovimentiGestioneAssociatiExcel" disabled="mutuo.elencoImpegniAssociati.empty && mutuo.elencoAccertamentiAssociati.empty"/>
                           		   </p>
		                     </dl>
						</div>
							
						<div class="tab-pane active" id="progetti">
							<dl class="dl-horizontal">
	                              <div class="boxOrInCenter">
	                               <h4>Progetti</h4>
	                               <s:if test="not mutuo.elencoProgettiAssociati.empty">
                              			<s:include value="/jsp/mutuo/include/elencoProgettiAssociatiMutuoConsulta.jsp" />
                              			 <p>
			                         		<s:submit value="esporta progetti associati in excel" cssClass="btn disableOverlay" action="consultaMutuo_scaricaProgettiAssociatiExcel" />
                           		   		</p>
                           		   </s:if><s:else>
                              			Non sono presenti progetti associati.
                           		   </s:else>
                           		   </div>
                           	</dl>
						</div>
						
						<div class="tab-pane active" id="ripartizione-capitoli">
							<dl class="dl-horizontal">
	                              <div class="boxOrInCenter">
	                              <h4>Ripartizione capitoli (rimborso prestito)</h4>
	                           	  <s:if test="not mutuo.elencoRipartizioneMutuo.empty">
	                              		<s:include value="/jsp/mutuo/include/elencoRipartizioneMutuoConsulta.jsp" />
	                              		 <p>
			                         	   <s:submit value="esporta ripartizione capitoli in excel" cssClass="btn disableOverlay" action="consultaMutuo_scaricaRipartizioneExcel" />
                           		   		</p>
	                              </s:if><s:else>
	                              		Non &egrave; presente alcuna ripartizione
	                           	  </s:else>
	
                           		   </div>
                           	</dl>
						</div>	
							
							
							        
							
					<p><s:include value="/jsp/include/indietro.jsp" /></p>
	
			</div>
	</s:form>
		</div>
	</div>
	</div>
	
    <s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/mutuo/consultaMutuo.js"></script>
	
 </body>
 </html>







